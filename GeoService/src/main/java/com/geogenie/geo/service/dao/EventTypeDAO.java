package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.EventType;

public interface EventTypeDAO {

	public EventType createEventType(EventType eventType);
	
	public List<EventType> getAllEventTypes();
	
	public List<EventType> getEventTypesByNames(List<String> names);
	
	public EventType getEventTypeByName(String name);
	
	public EventType save(EventType eventType);
}
