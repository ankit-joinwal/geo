package com.geogenie.geo.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.geo.service.business.ValidationService;

@RestController
public class BaseController {

	@Autowired
	private ValidationService validationService;
	
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	
}
