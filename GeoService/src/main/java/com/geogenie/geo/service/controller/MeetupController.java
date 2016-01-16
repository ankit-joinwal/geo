package com.geogenie.geo.service.controller;

import javax.servlet.http.HttpServletRequest;
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

import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.data.model.requests.CreateMeetupResponse;
import com.geogenie.geo.service.business.MeetupService;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/public/meetups")
public class MeetupController {

	private static final Logger logger = LoggerFactory.getLogger(MeetupController.class);
	
	@Autowired
	private MeetupService meetupService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public CreateMeetupResponse create(@Valid @RequestBody CreateMeetupRequest createMeetupRequest,HttpServletRequest  httpRequest){
		logger.info("### Request recieved- CreateMeetupRequest:  {} ###"+createMeetupRequest);
		logger.info("Request URL : {} ",httpRequest.getRequestURL());
		logger.info("Context Path : {} ",httpRequest.getContextPath());
		Transformer<CreateMeetupResponse, Meetup> transformer = (Transformer<CreateMeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		CreateMeetupResponse createMeetupResponse = transformer.transform(meetupService.createMetup(createMeetupRequest));
		createMeetupResponse.setUrl(httpRequest.getRequestURL()+"/"+createMeetupResponse.getUuid());
				
				
		return createMeetupResponse;
		
	}
	
	
	
}
