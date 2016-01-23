package com.geogenie.geo.service.business;

import java.util.List;

import com.geogenie.data.model.EventType;

public interface EventTypeService {

	public EventType createEventType(EventType eventType);
	
	public List<EventType> getAllEventTypes();
}
