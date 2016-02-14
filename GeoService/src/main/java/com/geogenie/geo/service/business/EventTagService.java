package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.geogenie.Constants;
import com.geogenie.data.model.EventTag;

public interface EventTagService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
}
