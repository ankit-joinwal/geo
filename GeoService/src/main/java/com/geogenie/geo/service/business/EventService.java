package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.geogenie.Constants;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.requests.CreateEventRequest;

public interface EventService {

	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public Event create(CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void makeEventLive(String eventId);
	
	public EventListResponse getEventsForUser(Long userId,String city,String country) ;
	
	public EventListResponse getEventsByType(String eventType,String city,String country);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void storeEventImages(List<MultipartFile> images , String eventId) ;
	
}
