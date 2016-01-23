package com.geogenie.geo.service.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.EventType;

@Repository("eventTagDAO")
public class EventTagDAOImpl extends AbstractDAO implements EventTagDAO {

	
	@Autowired
	private EventTypeDAO eventTypeDAO;
	@Override
	public EventTag create(EventTag eventTag) {

		
		Set<EventType> eventTypes = eventTag.getRelatedEventTypes();
		if(eventTypes!= null && !eventTypes.isEmpty()){
			List<String> eventTypeNames = new ArrayList<>();
			for(EventType eventType : eventTypes){
				eventTypeNames.add(eventType.getName());
			}
			List<EventType> relatedEventTypes = this.eventTypeDAO.getEventTypesByNames(eventTypeNames);
			eventTag.setRelatedEventTypes(new HashSet<>(relatedEventTypes));
		}
		saveOrUpdate(eventTag);
		return eventTag;
	}
	
	@Override
	public List<EventTag> getAll() {
		Criteria criteria = getSession().createCriteria(EventTag.class);
		return (List<EventTag>) criteria.list();
	}

	
	@Override
	public List<EventTag> getTagsByNames(List<String> names) {
		Criteria criteria = getSession().createCriteria(EventTag.class).add(Restrictions.in("name", names));
		return (List<EventTag>) criteria.list();
	}
}
