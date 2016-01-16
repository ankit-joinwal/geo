package com.geogenie.geo.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geogenie.Constants;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.User;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.geo.service.dao.MeetupDAO;
import com.geogenie.geo.service.dao.UserDAO;

@Service
@Transactional
public class MeetupServiceImpl implements MeetupService{

	private static final Logger logger = LoggerFactory.getLogger(MeetupServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MeetupDAO meetupDAO;
	
	@Override
	public Meetup createMetup(CreateMeetupRequest createMeetupRequest) {

		logger.info("### Inside MeetupServiceImpl.createMetup ###");
		Meetup meetup = new Meetup();
		User organizer = userDAO.getUserByEmailId(createMeetupRequest.getOrganizerId(), false); 
		logger.info("   Found organizer details in DB for {} : Id ",organizer.getEmailId(),organizer.getId());
		
		logger.info("   Loading attendees details for ids {} ",createMeetupRequest.getAttendees());
		List<User> users = userDAO.getUsersByExternalId(createMeetupRequest.getAttendees());
		
		
		 SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		 
		//Setting values into meetup
		meetup.setTitle(createMeetupRequest.getTitle());
		meetup.setDescription(createMeetupRequest.getDescription());
		meetup.setLocation(createMeetupRequest.getLocation());
		try {
			meetup.setStartDate(dateFormat.parse(createMeetupRequest.getStartDate()));
			meetup.setEndDate(dateFormat.parse(createMeetupRequest.getEndDate()));
		} catch (ParseException e) {

			logger.error("ParseException",e);
		}
		meetup.setAttendees(new HashSet<User>(users));
		meetup.setOrganizer(organizer);
		Meetup created = meetupDAO.createMeetup(meetup);
		return created;
	}
}
