package com.geogenie.geo.service.controller.secured;

import java.util.List;

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

import com.geogenie.data.model.EventTag;
import com.geogenie.geo.service.business.EventTagService;

@RestController
@RequestMapping("/api/secured/events/tags")
public class EventTagSecuredController {

	private static final Logger logger = LoggerFactory.getLogger(EventTagSecuredController.class);
	
	@Autowired
	private EventTagService eventTagService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public EventTag createEventTag(@Valid @RequestBody EventTag eventTag){
		logger.info("### Request recieved- createEventTag. Arguments : {} ###"+eventTag);
		
		
		return eventTagService.create(eventTag);
	}
	
	
}
