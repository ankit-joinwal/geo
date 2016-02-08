package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.geo.service.exception.ServiceException;

public interface EventService {

	public Event create(CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
	
	public void makeEventLive(String eventId);
	
	public EventListResponse getEventsForUser(Long userId,String city,String country) throws ServiceException;
	
	public EventListResponse getEventsByType(String eventType,String city,String country) throws ServiceException;
	
	public void storeEventImages(List<MultipartFile> images , String eventId) throws ServiceException;
	
}
