package com.geogenie.geo.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geogenie.Constants;
import com.geogenie.data.model.AttendeeResponse;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.data.model.requests.EditMeetupRequest;
import com.geogenie.data.model.requests.SaveAttendeeResponse;
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
		
		/* Do not allow inviting friends while creating meetup.
		logger.info("   Loading attendees details for ids {} ",createMeetupRequest.getAttendees());
		List<User> users = userDAO.getUsersByExternalId(createMeetupRequest.getAttendees());
		meetup.setAttendees(new HashSet<User>(users));
		*/
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
		
		meetup.setOrganizer(organizer);
		Meetup created = meetupDAO.createMeetup(meetup);
		
		
		return created;
	}
	 @Override
	public Meetup getMeetup(String meetupId) {
		 logger.info("### Inside MeetupServiceImpl.getMeetup ###");
		 
		 Meetup meetup = meetupDAO.getMeetup(meetupId);
		 if(meetup!=null){
			 logger.info("Found Meetup");
		 }
		return meetup;
	}
	 
	 
	 @Override
	public Meetup addAttendees(EditMeetupRequest editMeetupRequest) {
		 List<MeetupAttendee> meetupAttendees = editMeetupRequest.getAttendees();
		 Meetup meetup = this.meetupDAO.getMeetup(editMeetupRequest.getUuid());
		 Set<String> attendeeSocialIds = new HashSet<>();
		 
		 for(MeetupAttendee meetupAttendee : meetupAttendees){
			 attendeeSocialIds.add(meetupAttendee.getSocialDetail().getUserSocialDetail());
		 }
		 
		 Map<String,UserSocialDetail> socialIdVsSocialDetailMap = this.userDAO.getSocialDetails(attendeeSocialIds);
		 
		 if(socialIdVsSocialDetailMap!=null && !socialIdVsSocialDetailMap.isEmpty()){
			 for(MeetupAttendee meetupAttendee : meetupAttendees){
				 String socialId = meetupAttendee.getSocialDetail().getUserSocialDetail();
				 meetupAttendee.setSocialDetail(socialIdVsSocialDetailMap.get(socialId));
				 meetupAttendee.setMeetup(meetup);
			 }
		 }
		 meetup.getAttendees().addAll(new HashSet<>(meetupAttendees));
		 
		 meetup = this.meetupDAO.saveMeetup(meetup);
		 return meetup;
	}
	 
	 @Override
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse) {
		this.meetupDAO.saveAttendeeResponse(attendeeResponse);
		
	}
	 
	 @Override
	public void sendMessageInMeetup(MeetupMessage meetupMessage,
			String meetupId, Long senderId) {

		 this.meetupDAO.sendMessageInMeetup(meetupMessage, meetupId, senderId);
	}
}
