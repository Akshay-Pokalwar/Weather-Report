package com.weather.temperature.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.weather.temperature.service.ITemperatureService;


@RestController
@CrossOrigin(origins = "*")
public class TemperatureController {

	@Autowired
	private ITemperatureService temperatureService;
	
	@GetMapping("/getTempDetails/{zipCode}")
	public Object getTempDetails(@PathVariable("zipCode") String zipCode){
		return temperatureService.getTempDetails(zipCode);
		
	}
}

