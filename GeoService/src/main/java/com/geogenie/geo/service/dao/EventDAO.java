package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventImage;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.geo.service.exception.ServiceException;

public interface EventDAO {

	public Event create(Event event);
	
	public Event saveEvent(Event event);
	
	public Event getEvent(String id);
	
	public Event getEventWithoutImage(String id);
	
	public void makeEventLive(Event event);
	
	public EventListResponse getEventsByFilter(List<Long> tagIds,String city,String country) throws ServiceException;
	
	public void saveEventImages(List<EventImage> images);
	
	public List<EventImage> getEventImages(String eventId);
}
