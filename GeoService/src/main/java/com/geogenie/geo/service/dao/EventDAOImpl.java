package com.geogenie.geo.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.EventResponse;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.EventType;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@Repository("eventDAO")
public class EventDAOImpl extends AbstractDAO implements EventDAO {

	private static final Logger logger = LoggerFactory.getLogger(EventDAOImpl.class);
	@Override
	public Event create(Event event) {

		Date now = new Date();
		event.getEventDetails().setCreateDt(now);
		String eventId = (String) getSession().save(event);
		Event eventInDb = getEvent(eventId);
		
		return eventInDb;
	}

	
	@Override
	public Event saveEvent(Event event) {
		 saveOrUpdate(event);
		return event;
	}
	
	@Override
	public Event getEvent(String id) {
		Criteria criteria = getSession().createCriteria(Event.class).add(Restrictions.eq("uuid", id)).setFetchMode("image", FetchMode.JOIN)
				.setFetchMode("eventDetails", FetchMode.JOIN).setFetchMode("tags", FetchMode.JOIN);
		
		return (Event) criteria.uniqueResult();
	}
	
	@Override
	public void makeEventLive(String eventId) {
	
		Criteria criteria = getSession().createCriteria(Event.class).add(Restrictions.eq("uuid", eventId));
		Event event = (Event) criteria.uniqueResult();
		event.setIsLive("true");
		
	}
	
	@Override
	public EventListResponse getEventsBasedOnCityAndCountry(String city,
			String country) {
		 Criteria criteria = getSession().createCriteria(Event.class).setFetchMode("image", FetchMode.JOIN)
					.setFetchMode("eventDetails", FetchMode.JOIN).setFetchMode("eventDetails.organizer", FetchMode.JOIN)
					.setFetchMode("tags", FetchMode.SELECT)
					.createAlias("eventDetails", "ed")
					//.createAlias("eventDetails.addressComponents", "addressComp")
					//.createAlias("addressComp.addressComponentType", "addressCompType")
					.add(Restrictions.eq("isLive", "true"))
					.add(Restrictions.and(Restrictions.like("ed.location.name", city,MatchMode.ANYWHERE)
	        		,Restrictions.like("ed.location.name", country,MatchMode.ANYWHERE)));
	        
	        
	       /* Criterion cityCriteria = Restrictions.and(Restrictions.like("addressComp.value", "Delhi",MatchMode.ANYWHERE)
	        		,Restrictions.eq("addressCompType.name", "administrative_area_level_1"));
	        
	        Criterion countryCriteria = Restrictions.and(Restrictions.like("addressComp.value", "India",MatchMode.ANYWHERE)
	        		,Restrictions.eq("addressCompType.name", "country"));
	        
	        criteria.add(cityCriteria);
	        criteria.add(countryCriteria);
	        */
	        
		List<Event> events = criteria.list();
		EventListResponse eventListResponse = new EventListResponse();
		
		List<EventResponse> eventsInCity = new ArrayList<EventResponse>();
		if(events!=null){
			Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
			EventResponse eventInCity = null;
			for(Event event : events){
				//TODO: This is done to lazy load the tags.
				//If we use join fetch , then m*n records are pulled up.
				event.getTags().size();
				eventInCity = transformer.transform(event);
				eventsInCity.add(eventInCity);
			}
			eventListResponse.setEvents(eventsInCity);
			eventListResponse.setCount(eventsInCity.size());
		}
		return eventListResponse;
	}
	
	@Override
	public EventListResponse getEventsBasedOnTags(List<Long> tagIds,
			String city, String country) {
		 Criteria criteria = getSession().createCriteria(Event.class,"event").setFetchMode("image", FetchMode.JOIN)
					.setFetchMode("event.eventDetails", FetchMode.JOIN).setFetchMode("event.eventDetails.organizer", FetchMode.JOIN)
					.setFetchMode("event.tags", FetchMode.JOIN)
					.createAlias("event.eventDetails", "ed")
					.createAlias("event.tags", "eventTag")
					.add(Restrictions.eq("isLive", "true"))
					.add(Restrictions.in("eventTag.id",tagIds))
					.add(Restrictions.and(Restrictions.like("ed.location.name", city,MatchMode.ANYWHERE)
							,Restrictions.like("ed.location.name", country,MatchMode.ANYWHERE)))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 List<Event> events = criteria.list();
		 EventListResponse eventListResponse = new EventListResponse();
		 List<EventResponse> eventsResponse = new ArrayList<EventResponse>();
		 if(events!=null){
			 	logger.info("Found : {} events",events.size());
			 
				Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
				EventResponse eventInCity = null;
				for(Event event : events){
					//TODO: This is done to lazy load the tags.
					//If we use join fetch , then m*n records are pulled up.
					event.getTags().size();
					eventInCity = transformer.transform(event);
					eventsResponse.add(eventInCity);
				}
				eventListResponse.setEvents(eventsResponse);
				eventListResponse.setCount(eventsResponse.size());
			}
		 
		return eventListResponse;
	}
	
}
