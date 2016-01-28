package com.geogenie.geo.service.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.AddressComponentType;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.requests.SaveAttendeeResponse;

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
		logger.info("### Inside MeetupDAOImpl.getMeetup ");
		Criteria criteria = getSession().createCriteria(Meetup.class).add(Restrictions.eq("uuid", id)).setFetchMode("attendees", FetchMode.JOIN)
				.setFetchMode("eventAtMeetup",FetchMode.JOIN);
		return (Meetup) criteria.uniqueResult();
	}
	
	@Override
	public Meetup saveMeetup(Meetup meetup) {
		logger.info("### Inside MeetupDAOImpl.saveMeetup ");
		saveOrUpdate(meetup);
		return meetup;
	}
	
	@Override
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse) {
		MeetupAttendee meetupAttendee = (MeetupAttendee) getSession().get(MeetupAttendee.class, attendeeResponse.getAttendeeId());
		if(meetupAttendee!=null){
			logger.info("Storing attendee response for meetup {} for attendee {} ",attendeeResponse.getMeetupId(),attendeeResponse.getAttendeeId());
			meetupAttendee.setAttendeeResponse(attendeeResponse.getAttendeeResponse());
			getSession().saveOrUpdate(meetupAttendee);
		}
	}
	
	@Override
	public void sendMessageInMeetup(MeetupMessage meetupMessage,
			String meetupId, Long senderId) {
		MeetupAttendee meetupAttendee = (MeetupAttendee) getSession().get(MeetupAttendee.class, senderId);
		Meetup meetup = (Meetup) getSession().get(Meetup.class, meetupId);
		Date now = new Date();
		if(meetup!=null && meetupAttendee!=null){
			meetupMessage.setMeetup(meetup);
			meetupMessage.setMeetupAttendee(meetupAttendee);
			meetupMessage.setCreateDt(now);
			saveOrUpdate(meetupMessage);
		}
	}
	
	@Override
	public List<AddressComponentType> getAddressTypes() {
		Criteria criteria = getSession().createCriteria(AddressComponentType.class);
		
		return criteria.list();
	}
}
