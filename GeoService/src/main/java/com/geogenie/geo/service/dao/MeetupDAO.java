package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.AddressComponentType;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.requests.SaveAttendeeResponse;

public interface MeetupDAO {

	public Meetup createMeetup(Meetup meetup);
	
	public Meetup getMeetup(String id);
	
	public Meetup saveMeetup(Meetup meetup);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,Long senderId);
	
	public List<AddressComponentType> getAddressTypes();
}
