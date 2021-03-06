package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.EventTag;

public interface EventTagDAO {

	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
	
	public List<EventTag> getTagsByNames(List<String> names);
	
	public List<EventTag> getUserTags(Long userId);
	
	public List<Long> getUserTagIds(Long userId);
	
	public List<EventTag> saveUserTagPreferences(List<EventTag> tags,Long userId);
}
