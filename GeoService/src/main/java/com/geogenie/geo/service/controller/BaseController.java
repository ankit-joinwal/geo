package com.geogenie.geo.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.geo.service.business.ValidationService;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

@RestController
public class BaseController {

	@Autowired
	private ValidationService validationService;
	
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	protected Object validateRequest(String authorization){
		if(authorization==null || authorization.isEmpty()){
			return new ServiceException(ServiceErrorCodes.ERR_002,"Authentication missing");
		}
		return validationService.validateReuqest(authorization);
	}
}
