package com.geogenie.geo.service.controller.secured;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.EventType;
import com.geogenie.geo.service.business.EventTypeService;

@RestController
@RequestMapping("/api/secured/events/types")
public class EventTypeSecuredController {

	private static final Logger logger = LoggerFactory.getLogger(EventTypeSecuredController.class);
	
	@Autowired
	private EventTypeService eventService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public EventType createEventType(@Valid @RequestBody EventType eventType){
		logger.info("### Request recieved- createEventType. Arguments : {} ###"+eventType);
		return eventService.createEventType(eventType);
	}
	
	
	
}
