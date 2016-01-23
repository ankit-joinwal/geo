package com.geogenie.geo.service.business;

import java.util.List;

import com.geogenie.data.model.EventTag;

public interface EventTagService {

	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
}
