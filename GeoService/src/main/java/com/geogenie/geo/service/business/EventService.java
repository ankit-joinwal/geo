package com.geogenie.geo.service.business;

import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;

public interface EventService {

	public Event create(CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
	
	public void makeEventLive(String eventId);
	
	public EventListResponse getEventsInCity(String city,String country);
	
	public EventListResponse getEventsByType(String eventType,String city,String country);
	
}
