package com.geogenie.geo.service.dao;

import com.geogenie.data.model.Meetup;

public interface MeetupDAO {

	public Meetup createMeetup(Meetup meetup);
	
	public Meetup getMeetup(String id);
}
