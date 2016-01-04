package com.geogenie.geo.service.helper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.geogenie.data.model.ext.Places;

public class Test {
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Places> placesResponse = restTemplate
				.exchange(
						"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.5688764,77.2256099&radius=100&types=atm&name=Axis+Bank&key=AIzaSyDjWU2Jk74xtz7maoP76e5BwPWDbILosyQ",
						HttpMethod.GET, null,
						new ParameterizedTypeReference<Places>() {
						});

		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			System.out.println("Successfull");
		} else {
			System.out.println("Error");
		}
	}
}
