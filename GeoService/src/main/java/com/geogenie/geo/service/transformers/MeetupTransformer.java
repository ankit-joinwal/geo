package com.geogenie.geo.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.geogenie.Constants;
import com.geogenie.data.model.Meetup;
import com.geogenie.data.model.MeetupMessage;
import com.geogenie.data.model.requests.CreateMeetupResponse;

public class MeetupTransformer implements Transformer<CreateMeetupResponse, Meetup> {

	
	@Override
	public CreateMeetupResponse transform(Meetup meetup) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		CreateMeetupResponse createMeetupResponse = new CreateMeetupResponse();
		
		createMeetupResponse.setAttendees(meetup.getAttendees());
		createMeetupResponse.setDescription(meetup.getDescription());
		createMeetupResponse.setLocation(meetup.getLocation());
		createMeetupResponse.setOrganizer(meetup.getOrganizer());
		createMeetupResponse.setUuid(meetup.getUuid());
		createMeetupResponse.setTitle(meetup.getTitle());
		
		Date startDate = meetup.getStartDate();
		Date endDate = meetup.getEndDate();
		createMeetupResponse.setStartDate(dateFormat.format(startDate));
		createMeetupResponse.setEndDate(dateFormat.format(endDate));
		
		Date now = new Date();
		for(MeetupMessage meetupMessage : meetup.getMessages()){
			Date messageTime = meetupMessage.getCreateDt();
			long diff = now.getTime() - messageTime.getTime();//in millisecons
			
			int diffDays = (int)(diff / (24 * 60 * 60 * 1000));
			
			if(diffDays>0){
				meetupMessage.setTimeToDisplay(diffDays+"Day ago");
				continue;
			}
			
			int diffHours = (int)(diff / (60 * 60 * 1000) % 24);
			if(diffHours>0){
				meetupMessage.setTimeToDisplay(diffHours+"Hour ago");
				continue;
			}
			
			int diffMinutes =(int) (diff / (60 * 1000) % 60);
			if(diffMinutes>0){
				meetupMessage.setTimeToDisplay(diffMinutes+"Min ago");
				continue;
			}
			
			meetupMessage.setTimeToDisplay("Just Now");
		}
		createMeetupResponse.setMessages(meetup.getMessages());
		
		return createMeetupResponse;
	}
}
