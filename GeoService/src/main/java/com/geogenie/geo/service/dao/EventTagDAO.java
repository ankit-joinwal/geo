package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.EventTag;

public interface EventTagDAO {

	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
	
	public List<EventTag> getTagsByNames(List<String> names);
}
