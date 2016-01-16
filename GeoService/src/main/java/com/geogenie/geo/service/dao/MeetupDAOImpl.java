package com.geogenie.geo.service.dao;


import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.Meetup;

@Repository("meetupDAO")
public class MeetupDAOImpl extends AbstractDAO implements MeetupDAO{
	private static final Logger logger = LoggerFactory.getLogger(MeetupDAOImpl.class);
	
	
	@Override
	public Meetup createMeetup(Meetup meetup) {
		
		logger.info("### Inside MeetupDAOImpl.createMeetup ");
		
		
		String meetupId = (String) getSession().save(meetup);
		
		return getMeetup(meetupId);
	}
	
	@Override
	public Meetup getMeetup(String id) {
		Criteria criteria = getSession().createCriteria(Meetup.class).add(Restrictions.eq("uuid", id)).setFetchMode("attendees", FetchMode.JOIN);
		
		
		return (Meetup) criteria.uniqueResult();
	}
}
