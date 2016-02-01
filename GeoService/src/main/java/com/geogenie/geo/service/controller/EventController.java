package com.geogenie.geo.service.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventImage;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.EventResponse;
import com.geogenie.geo.service.business.EventService;
import com.geogenie.geo.service.exception.ServiceException;
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
	public EventResponse create(@Valid @RequestBody CreateEventRequest createEventRequest) throws ServiceException{
		logger.info("### Request recieved - create event {} ",createEventRequest) ;
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.create(createEventRequest));
		
		return createEventResponse;
	}
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EventResponse getEvent(@PathVariable String eventId) throws ServiceException{
		logger.info("### Request recieved - get event {} ",eventId);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		
		return createEventResponse;
	}
	
	@RequestMapping(value = "/{eventId}/images/upload", method = RequestMethod.POST)
    public void  upload(@PathVariable String eventId,MultipartHttpServletRequest request,
                  HttpServletResponse response) throws IOException,ServiceException {

		logger.info("Request recieved to upload event images for event {} ",eventId);
           // Getting uploaded files from the request object
           Map<String, MultipartFile> fileMap = request.getFileMap();
          if(fileMap.values()!=null && !fileMap.values().isEmpty()){
        	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
        	  this.eventService.storeEventImages(files, eventId);
          }
          
    }
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void makeEventLive(@PathVariable String eventId){
		logger.info("### Request Recieved - make Event Live ###");
		this.eventService.makeEventLive(eventId);
	}
	
	@RequestMapping(value="/personalized",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EventListResponse getEventsInCity(@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country) throws ServiceException{

		logger.info("### Request Recieved - getEventsInCity. City {} , Country {}  ###",city,country);
		
		return this.eventService.getEventsInCity(city, country);
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
