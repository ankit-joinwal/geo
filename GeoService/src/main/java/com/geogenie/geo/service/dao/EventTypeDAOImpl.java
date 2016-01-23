package com.geogenie.geo.service.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.Category;
import com.geogenie.data.model.EventType;

@Repository("eventTypeDao")
public class EventTypeDAOImpl extends AbstractDAO implements EventTypeDAO {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	public EventType createEventType(EventType eventType) {
		
		Set<Category> relatedCategories = eventType.getRelatedCategories();
		
		if(relatedCategories!=null && !relatedCategories.isEmpty()){
			List<String> catNameList = new ArrayList<>();
			for(Category category:relatedCategories){
				catNameList.add(category.getName());
				
			}
			List<Category> categories = categoryDAO.getCategoriesByName(catNameList);
			Set<Category> categoriesToSaveWith = new HashSet<>(categories);
			eventType.setRelatedCategories(categoriesToSaveWith);
		}
		saveOrUpdate(eventType);
		return eventType;
	}
	
	@Override
	public List<EventType> getAllEventTypes() {
		Criteria criteria = getSession()
				.createCriteria(EventType.class).setFetchMode("relatedTags", FetchMode.JOIN);
		return (List<EventType>) criteria.list();
	}
	
	@Override
	public List<EventType> getEventTypesByNames(List<String> names) {
		Criteria criteria = getSession().createCriteria(EventType.class).add(Restrictions.in("name", names)).setFetchMode("relatedTags", FetchMode.JOIN);
		List<EventType> eventTypes = (List<EventType>) criteria.list();
		return eventTypes;
	}

}
