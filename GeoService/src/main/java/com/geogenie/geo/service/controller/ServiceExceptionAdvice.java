package com.geogenie.geo.service.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.geogenie.geo.service.exception.MessageDTO;
import com.geogenie.geo.service.exception.MessageDTO.MessageType;
import com.geogenie.geo.service.exception.ServiceException;

@ControllerAdvice
public class ServiceExceptionAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionAdvice.class);
	
	@Autowired
	private MessageSource msgSource;

	
	
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public MessageDTO processServerError(ServiceException ex) {

		MessageDTO message = null;
		LOGGER.error("ServiceException occurred .",ex);
		message = new MessageDTO("Invalid Request", MessageType.ERROR,ex);
		return message;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public MessageDTO processValidationError(MethodArgumentNotValidException ex) {
		LOGGER.error("MethodArgumentNotValidException occurred .",ex);
		BindingResult result = ex.getBindingResult();
		FieldError error = result.getFieldError();
		return processFieldError(error);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public MessageDTO processServerError(Exception ex) {

		LOGGER.error("Exception occurred .",ex);
		MessageDTO message = null;

		message = new MessageDTO(ex.getMessage(), MessageType.ERROR);

		return message;
	}
	
	private MessageDTO processFieldError(FieldError error) {
		MessageDTO message = null;
		if (error != null) {
			Locale currentLocale = LocaleContextHolder.getLocale();
			String msg = msgSource.getMessage(error.getDefaultMessage(), null,
					currentLocale);
			message = new MessageDTO(msg, MessageType.ERROR);
		}
		return message;
	}

}
