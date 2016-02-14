package com.geogenie.geo.service.controller.secured;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventResponse;
import com.geogenie.data.model.requests.CreateEventRequest;
import com.geogenie.geo.service.business.EventService;
import com.geogenie.geo.service.exception.ClientException;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/secured/events")
public class EventSecuredController {

	private static final Logger logger = LoggerFactory.getLogger(EventSecuredController.class);
	
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
	
	@RequestMapping(value = "/{eventId}/images/upload", method = RequestMethod.POST)
    public void upload(@PathVariable String eventId,MultipartHttpServletRequest request,
                  HttpServletResponse response) throws ClientException,ServiceException {

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
	
}
