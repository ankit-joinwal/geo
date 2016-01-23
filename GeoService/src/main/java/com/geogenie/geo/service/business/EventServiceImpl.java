package com.geogenie.geo.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geogenie.Constants;
import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventDetails;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.User;
import com.geogenie.geo.service.dao.EventDAO;
import com.geogenie.geo.service.dao.EventTagDAO;
import com.geogenie.geo.service.dao.UserDAO;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private EventTagDAO eventTagDAO;
	
	@Override
	public Event create(CreateEventRequest createEventRequest) {
		logger.info("### Inside CreateEventRequest.create ###");
		Event event = new Event();
		EventDetails eventDetails = createEventRequest.getEventDetails();
		User organizer = this.userDAO.getUserByEmailId(createEventRequest.getOrganizerId(), false); 
		logger.info("   Found organizer details in DB for {} : Id {}",organizer.getEmailId(),organizer.getId());
		
		Set<EventTag> tags = createEventRequest.getTags();
		if(tags!=null && !tags.isEmpty()){
			List<String> tagNames = new ArrayList<>();
			for(EventTag eventTag : tags){
				tagNames.add(eventTag.getName());
			}
			List<EventTag> tagsInDB = eventTagDAO.getTagsByNames(tagNames);
			event.setTags(new HashSet<>(tagsInDB));
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		try {
			event.setStartDate(dateFormat.parse(createEventRequest.getStartDate()));
			event.setEndDate(dateFormat.parse(createEventRequest.getEndDate()));
		} catch (ParseException e) {

			logger.error("ParseException",e);
		}
		 
		eventDetails.setOrganizer(organizer);
		event.setTitle(createEventRequest.getTitle());
		event.setDescription(createEventRequest.getDescription());
		event.setImage(createEventRequest.getImage());
		event.setEventDetails(eventDetails);
		
		return this.eventDAO.create(event);
	}
	
	@Override
	public Event get(String uuid) {
		
		return this.eventDAO.getEvent(uuid);
	}

}
