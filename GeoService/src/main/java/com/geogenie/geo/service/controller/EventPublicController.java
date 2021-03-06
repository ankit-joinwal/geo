package com.geogenie.geo.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.EventResponse;
import com.geogenie.geo.service.business.EventService;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/public/events")
public class EventPublicController {
	private static final Logger logger = LoggerFactory.getLogger(EventPublicController.class);
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EventResponse getEvent(@PathVariable String eventId) throws ServiceException{
		logger.info("### Request recieved - get event {} ",eventId);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		
		return createEventResponse;
	}
	
	@RequestMapping(value="/personalized",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EventListResponse getPersonalizedEvents(@RequestParam(required = false, value = "id") Long userId,@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country) throws ServiceException{

		logger.info("### Request Recieved - getPersonalizedEvents. City {} , Country {} ,user {} ###",city,country,userId);
		
		return this.eventService.getEventsForUser(userId,city, country);
	}
	
	

	@RequestMapping(value="/types/{eventType}",method=RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EventListResponse getEventsOfType(@PathVariable String eventType,@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country) throws ServiceException{
		logger.info("### Request Recieved - getEventsOfType - Params [ Type :{} , City : {} , Country : {} ###",eventType,city,country);
		return this.eventService.getEventsByType(eventType, city, country);
	}
	
}
