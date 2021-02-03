package com.weather.temperature.service.impl;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.weather.temperature.service.ITemperatureService;

@Service
@PropertySource(value = { "classpath:application.properties" })
public class TemperatureServiceImpl implements ITemperatureService{

	@Value("${weather.api.key}")
	private String API_ID;
	private static final String LAT = "LATTITUDE";
	private static final String LON = "LONGITUDE";
	private static final String API_KEY = "API_KEY";
	private static final String ZIP_CODE = "ZIP_CODE";
//	private static final String API_ID = "a1e4246c8cb7f85155b71744841118fb";
	private String BASIC_DETAILS_URL = "http://api.openweathermap.org/data/2.5/forecast?zip=" + ZIP_CODE
			+ "&exclude=minutely,daily&cnt=24&appid=" + API_KEY;
	private String TEMP_DETAILS_URL = "http://api.openweathermap.org/data/2.5/onecall?lat=" + LAT + "&lon=" + LON
			+ "&exclude=minutely,daily,current,alerts&cnt=1&appid=" + API_KEY;

	@Override
	public Object getTempDetails(String zipCode) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			BASIC_DETAILS_URL = replaceString(BASIC_DETAILS_URL, ZIP_CODE, zipCode);
			BASIC_DETAILS_URL = replaceString(BASIC_DETAILS_URL, API_KEY, API_ID);
			String basicDetails = restTemplate.getForObject(BASIC_DETAILS_URL, String.class);
			if(basicDetails.length() != 0) {			
				JSONObject basicDetailsJSON = convertToJSONObject(basicDetails);
				JSONObject cityDetails = getDetails(basicDetailsJSON,"city");
				JSONObject coordinates = getDetails(cityDetails,"coord");
				setCoordinates(coordinates);
				String tempDetails = restTemplate.getForObject(TEMP_DETAILS_URL, String.class);
				return convertToJSONObject(tempDetails).toMap();
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return "Invalid zip-code";
	}

	private JSONObject convertToJSONObject(String details) {
	return new JSONObject(details);
}

	private JSONObject getDetails(JSONObject details,String key) {
		return details.getJSONObject(key);
	}
	private void setCoordinates(JSONObject coordinates) {
		BigDecimal longitude = (BigDecimal) coordinates.get("lon");
		BigDecimal lattitude = (BigDecimal) coordinates.get("lat");
		TEMP_DETAILS_URL = replaceString(TEMP_DETAILS_URL, LON, longitude.toString());
		TEMP_DETAILS_URL = replaceString(TEMP_DETAILS_URL, LAT, lattitude.toString());
		TEMP_DETAILS_URL = replaceString(TEMP_DETAILS_URL, API_KEY, API_ID);
	}

	public String replaceString(String str, String placeholder, String replacement) {
		return str.replaceAll(placeholder, replacement);
	}

}
