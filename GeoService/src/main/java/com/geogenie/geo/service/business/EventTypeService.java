package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.geogenie.Constants;
import com.geogenie.data.model.EventType;

public interface EventTypeService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public EventType createEventType(EventType eventType);
	
	public List<EventType> getAllEventTypes();
}
