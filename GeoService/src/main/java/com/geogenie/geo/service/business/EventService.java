package com.geogenie.geo.service.business;

import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.Event;

public interface EventService {

	public Event create(CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
}
