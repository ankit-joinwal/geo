package com.geogenie.geo.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.geogenie.Constants;
import com.geogenie.data.model.CreateEventResponse;
import com.geogenie.data.model.Event;

public class EventTransformer implements Transformer<CreateEventResponse, Event> {

	
	@Override
	public CreateEventResponse transform(Event event) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		CreateEventResponse createEventResponse = new CreateEventResponse();
		//TODO
		//createEventResponse.setAttendees(meetup.getAttendees());
		
		createEventResponse.setDescription(event.getDescription());
		createEventResponse.setEventDetails(event.getEventDetails());
		createEventResponse.setUuid(event.getUuid());
		createEventResponse.setTitle(event.getTitle());
		createEventResponse.setImage(event.getImage());
		createEventResponse.setTags(event.getTags());
		Date startDate = event.getStartDate();
		Date endDate = event.getEndDate();
		createEventResponse.setIsLive(event.getIsLive());
		createEventResponse.setStartDate(dateFormat.format(startDate));
		createEventResponse.setEndDate(dateFormat.format(endDate));
		
		
		
		return createEventResponse;
	}
}
