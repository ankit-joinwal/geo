package com.geogenie.data.model.requests;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.geogenie.data.model.MeetupAttendee;

@XmlRootElement(name="edit_meetup")
public class EditMeetupRequest{
	
	@JsonIgnore
	private String uuid;
	
	
	@NotNull(message="error.attendees.mandatory")
	@JsonProperty
	@XmlElement
	private List<MeetupAttendee> attendees;

	public List<MeetupAttendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<MeetupAttendee> attendees) {
		this.attendees = attendees;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	
	
}
