package com.geogenie.geo.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.geogenie.Constants;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.requests.CreateMeetupResponse;

public class MeetupTransformer implements Transformer<CreateMeetupResponse, Meetup> {

	
	@Override
	public CreateMeetupResponse transform(Meetup meetup) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		CreateMeetupResponse createMeetupResponse = new CreateMeetupResponse();
		
		createMeetupResponse.setAttendees(meetup.getAttendees());
		createMeetupResponse.setDescription(meetup.getDescription());
		createMeetupResponse.setLocation(meetup.getLocation());
		createMeetupResponse.setOrganizer(meetup.getOrganizer());
		createMeetupResponse.setUuid(meetup.getUuid());
		createMeetupResponse.setTitle(meetup.getTitle());
		
		Date startDate = meetup.getStartDate();
		Date endDate = meetup.getEndDate();
		createMeetupResponse.setStartDate(dateFormat.format(startDate));
		createMeetupResponse.setEndDate(dateFormat.format(endDate));
		
		return createMeetupResponse;
	}
}
