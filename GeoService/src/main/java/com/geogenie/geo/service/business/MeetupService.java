package com.geogenie.geo.service.business;

import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.data.model.requests.EditMeetupRequest;
import com.geogenie.data.model.requests.SaveAttendeeResponse;

public interface MeetupService {

	public Meetup createMetup(CreateMeetupRequest createMeetupRequest);
	
	public Meetup getMeetup(String meetupId);
	
	public Meetup addAttendees(EditMeetupRequest editMeetupRequest);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,String senderId);
}
