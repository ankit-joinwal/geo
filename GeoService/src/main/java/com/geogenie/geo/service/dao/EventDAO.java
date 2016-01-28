package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.EventResponse;

public interface EventDAO {

	public Event create(Event event);
	
	public Event saveEvent(Event event);
	
	public Event getEvent(String id);
	
	public void makeEventLive(String eventId);
	
	public EventListResponse getEventsBasedOnCityAndCountry(String city,String country);
	
	public EventListResponse getEventsBasedOnTags(List<Long> tagIds,String city,String country);
	
	
}
