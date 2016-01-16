package com.geogenie.geo.service.business;

import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.requests.CreateMeetupRequest;

public interface MeetupService {

	public Meetup createMetup(CreateMeetupRequest createMeetupRequest);
}
