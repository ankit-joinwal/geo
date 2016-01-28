package com.geogenie.geo.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.geogenie.data.model.AddressComponentType;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupAddressInfo;
import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.data.model.ext.PlaceDetails;
import com.geogenie.data.model.ext.PlaceDetails.Result.AddressComponent;
import com.geogenie.data.model.requests.CreateMeetupRequest;
import com.geogenie.data.model.requests.EditMeetupRequest;
import com.geogenie.data.model.requests.SaveAttendeeResponse;
import com.geogenie.geo.service.dao.EventDAO;
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
	
	@Autowired
	private EventDAO eventDAO;
	
	@Override
	public Meetup createMetup(CreateMeetupRequest createMeetupRequest) {

		logger.info("### Inside MeetupServiceImpl.createMetup ###");
		Meetup meetup = new Meetup();
		User organizer = userDAO.getUserByEmailId(createMeetupRequest.getOrganizerId(), false); 
		logger.info("   Found organizer details in DB for {} : Id ",organizer.getEmailId(),organizer.getId());

		Event eventAtMeetup = null;
		boolean isMeetupAtEvent = false;
		if(createMeetupRequest.getEventAtMeetup()!=null && !createMeetupRequest.getEventAtMeetup().isEmpty()){
			isMeetupAtEvent = true;
			eventAtMeetup = this.eventDAO.getEvent(createMeetupRequest.getEventAtMeetup());
			meetup.setEventAtMeetup(eventAtMeetup);
		}
		
		
		//Setting values into meetup
		meetup.setTitle(createMeetupRequest.getTitle());
		meetup.setDescription(createMeetupRequest.getDescription());
		meetup.setLocation(createMeetupRequest.getLocation());
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
			meetup.setStartDate(dateFormat.parse(createMeetupRequest.getStartDate()));
			meetup.setEndDate(dateFormat.parse(createMeetupRequest.getEndDate()));
		} catch (ParseException e) {
			logger.error("ParseException",e);
		}
		meetup.setOrganizer(organizer);
		Meetup created = meetupDAO.createMeetup(meetup);
		if(isMeetupAtEvent){
			//TODO: Set address components
		}else{
			created.setAddressComponents(this.getMeetupAddressInfo(created,createMeetupRequest.getAddressComponents()));
		}
		
		meetupDAO.saveMeetup(created);
		return created;
	}
	
	private Set<MeetupAddressInfo> getMeetupAddressInfo(Meetup meetup,Set<PlaceDetails.Result.AddressComponent> addressComponents){
		List<AddressComponentType> addressComponentTypes = this.meetupDAO.getAddressTypes();
		logger.info("Inside getMeetupAddressInfo. addressComponentTypes : {}  ",addressComponentTypes);
		Map<String,AddressComponentType> addressComponentTypesMap = new HashMap<>();
		for(AddressComponentType addressComponentType: addressComponentTypes){
			addressComponentTypesMap.put(addressComponentType.getName(), addressComponentType);
		}
		Set<MeetupAddressInfo> meetupAddresses = new HashSet<>();
		for(AddressComponent addressComponent : addressComponents){
			logger.info("Address Component {} "+addressComponent.getLongName());
			List<String> types = addressComponent.getTypes();
			logger.info("Types : {} ",types);
			for(String type : types){
				if(addressComponentTypes.contains(new AddressComponentType(type))){
					logger.info("Address component type found : {}",type);
					
					AddressComponentType addressComponentType = addressComponentTypesMap.get(type);
					MeetupAddressInfo meetupAddressInfo  = new MeetupAddressInfo();
					meetupAddressInfo.setAddressComponentType(addressComponentType);
					meetupAddressInfo.setValue(addressComponent.getLongName());
					meetupAddressInfo.setMeetup(meetup);
					meetupAddresses.add(meetupAddressInfo);
				
					continue;
				}
			}
		}
		
		logger.info("Meetup Address Components : " + meetupAddresses);
		
		return meetupAddresses;
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
			String meetupId, String userSocialId) {

		 UserSocialDetail userSocialDetail = this.userDAO.getSocialDetail(userSocialId);
		 MeetupAttendee meetupAttendee = this.userDAO.getAttendeeByMeetupIdAndSocialId(meetupId, userSocialDetail.getId());
		 if(meetupAttendee==null){
			 logger.error("MeetupAttendee not found for social id {} , meetup {} ",userSocialId,meetupId);
		 }else{
			 this.meetupDAO.sendMessageInMeetup(meetupMessage, meetupId, meetupAttendee.getAttendeeId());
		 }
	}
}
