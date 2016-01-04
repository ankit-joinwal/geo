package com.geogenie.geo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.UNAUTHORIZED,reason="Unauthorized request")
public class UnauthorizedException extends ServiceException{

	
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super();
	}
}
