package com.geogenie.geo.service.dao;

import com.geogenie.data.model.Event;

public interface EventDAO {

	public Event create(Event event);
	
	public Event getEvent(String id);
}
