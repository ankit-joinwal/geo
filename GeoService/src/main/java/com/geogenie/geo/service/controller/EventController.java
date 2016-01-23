package com.geogenie.geo.service.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.CreateEventResponse;
import com.geogenie.data.model.Event;
import com.geogenie.geo.service.business.EventService;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/public/events")
public class EventController {

	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public CreateEventResponse create(@Valid @RequestBody CreateEventRequest createEventRequest){
		logger.info("### Request recieved - create event {} ",createEventRequest);
		Transformer<CreateEventResponse, Event> transformer = (Transformer<CreateEventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		CreateEventResponse createEventResponse = transformer.transform(eventService.create(createEventRequest));
		
		return createEventResponse;
	}
	
	@RequestMapping(value="{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public CreateEventResponse getEvent(@PathVariable String eventId){
		logger.info("### Request recieved - get event {} ",eventId);
		Transformer<CreateEventResponse, Event> transformer = (Transformer<CreateEventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		CreateEventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		
		return createEventResponse;
	}
}
