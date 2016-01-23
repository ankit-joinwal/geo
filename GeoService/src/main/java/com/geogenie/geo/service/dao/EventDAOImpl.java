package com.geogenie.geo.service.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.Event;

@Repository("eventDAO")
public class EventDAOImpl extends AbstractDAO implements EventDAO {

	@Override
	public Event create(Event event) {

		Date now = new Date();
		event.getEventDetails().setCreateDt(now);
		String eventId = (String) getSession().save(event);
		Event eventInDb = getEvent(eventId);
		
		return eventInDb;
	}

	@Override
	public Event getEvent(String id) {
		Criteria criteria = getSession().createCriteria(Event.class).add(Restrictions.eq("uuid", id)).setFetchMode("image", FetchMode.JOIN)
				.setFetchMode("eventDetails", FetchMode.JOIN).setFetchMode("tags", FetchMode.JOIN);
		
		return (Event) criteria.uniqueResult();
	}
}
