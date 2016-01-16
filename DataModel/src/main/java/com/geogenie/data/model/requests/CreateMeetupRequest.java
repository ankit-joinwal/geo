package com.geogenie.data.model.requests;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.geogenie.data.model.Location;

@XmlRootElement(name="meetup")
@XmlAccessorType(XmlAccessType.NONE)
public class CreateMeetupRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@NotNull
	private Location location;
	
	@NotNull
	private String startDate;
	
	@NotNull
	private String endDate;
	
	@NotNull
	private String organizerId;
	
	@NotNull
	private List<String> attendees;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public List<String> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}
	
	
	@Override
	public String toString() {
		return "meetupRequest [ title = "+this.title+" , description = "+this.description+" , location = "+ location.toString()+ 
				" , organizer = "+this.organizerId + " , startDate = "+this.startDate + " , endDate = "+this.endDate + " ]";
	}

}
