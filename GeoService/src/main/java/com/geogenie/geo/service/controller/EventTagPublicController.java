package com.geogenie.geo.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.EventTag;
import com.geogenie.geo.service.business.EventTagService;

@RestController
@RequestMapping("/api/public/events/tags")
public class EventTagPublicController {

private static final Logger logger = LoggerFactory.getLogger(EventTagPublicController.class);
	
	@Autowired
	private EventTagService eventTagService;
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public List<EventTag> getAllEventTags(){
		logger.info("### Request recieved- getAllEventTags.  ###");
		
		
		return eventTagService.getAll();
	}
	
	
}
