package com.geogenie.data.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.NONE)
public class CreateEventResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String uuid;

	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@NotNull
	private EventImage image;
	
	@NotNull
	private EventDetails eventDetails;
	
	@NotNull
	private String startDate;
	
	@NotNull
	private String endDate;
	
	private Set<EventTag> tags = new HashSet<>();
	
	private String isLive;
	

	public String getIsLive() {
		return isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}

	public Set<EventTag> getTags() {
		return tags;
	}

	public void setTags(Set<EventTag> tags) {
		this.tags = tags;
	}

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

	public EventImage getImage() {
		return image;
	}

	public void setImage(EventImage image) {
		this.image = image;
	}

	public EventDetails getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(EventDetails eventDetails) {
		this.eventDetails = eventDetails;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	
	
}
