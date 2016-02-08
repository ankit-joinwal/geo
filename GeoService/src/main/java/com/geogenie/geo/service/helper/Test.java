package com.geogenie.geo.service.helper;

import java.io.File;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.geogenie.data.model.ext.Places;

public class Test {
	public static void main(String[] args)throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Places places = mapper.readValue(new File("D:\\testdata\\places.json"), Places.class);
		System.out.println("Places :"+places.getResults());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		ResponseEntity<Places> placesResponse = restTemplate.exchange(
				"https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+kalkaji&key=AIzaSyDjWU2Jk74xtz7maoP76e5BwPWDbILosyQ"
				//"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.5401956,77.1786472&radius=1000&types=atm&key=AIzaSyDjWU2Jk74xtz7maoP76e5BwPWDbILosyQ"
				, HttpMethod.GET, null,
				new ParameterizedTypeReference<Places>() {
				});
		
		System.out.println("Places : "+placesResponse.getBody().getResults());
	}
}
