package com.geogenie.geo.service.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.data.model.requests.CreateMeetupResponse;
import com.geogenie.data.model.requests.EditMeetupRequest;
import com.geogenie.data.model.requests.SaveAttendeeResponse;
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
	
	@RequestMapping(value="/{meetupId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public CreateMeetupResponse getMeetup(@PathVariable String meetupId){
		logger.info("### Request recieved- getMeetup : {} ###",meetupId);
		Transformer<CreateMeetupResponse, Meetup> transformer = (Transformer<CreateMeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		CreateMeetupResponse createMeetupResponse = transformer.transform(meetupService.getMeetup(meetupId));

		return createMeetupResponse;
		
	}
	
	@RequestMapping(value="/{meetupId}/attendees",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public CreateMeetupResponse addAttendees(@Valid @RequestBody EditMeetupRequest editMeetupRequest,@PathVariable String meetupId){
		logger.info("### Request recieved- editMeetup : {} ###",editMeetupRequest);
		Transformer<CreateMeetupResponse, Meetup> transformer = (Transformer<CreateMeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		
		editMeetupRequest.setUuid(meetupId);
		CreateMeetupResponse createMeetupResponse = transformer.transform(meetupService.addAttendees(editMeetupRequest));

		return createMeetupResponse;
	}
	
	@RequestMapping(value="/{meetupId}/attendees/{attendeeId}/response",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void saveResponse(@Valid @RequestBody SaveAttendeeResponse saveAttendeeResponse,@PathVariable String meetupId,@PathVariable Long attendeeId){
		logger.info("### Request recieved- SaveAttendeeResponse : {} for meetup {} , attendee {} ###",saveAttendeeResponse,meetupId,attendeeId);
		saveAttendeeResponse.setAttendeeId(attendeeId);
		saveAttendeeResponse.setMeetupId(meetupId);
		this.meetupService.saveAttendeeResponse(saveAttendeeResponse);
	}
	
	
	@RequestMapping(value="/{meetupId}/attendees/{userSocialId}/message",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void createMesssage(@Valid @RequestBody MeetupMessage meetupMessage,@PathVariable String meetupId,@PathVariable String userSocialId){
		logger.info("### Request recieved- SendMessage : {} for meetup {} , user social id {} ###",meetupMessage,meetupId,userSocialId);
		this.meetupService.sendMessageInMeetup(meetupMessage, meetupId, userSocialId);
	}
	
	@RequestMapping(value="/{meetupId}/messages",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public Set<MeetupMessage> getMeetupMessages(@PathVariable String meetupId){
		Transformer<CreateMeetupResponse, Meetup> transformer = (Transformer<CreateMeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		CreateMeetupResponse createMeetupResponse = transformer.transform(meetupService.getMeetup(meetupId));
		return createMeetupResponse.getMessages();
	}
	
}
