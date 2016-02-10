package com.geogenie.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="MEETUP")
@XmlRootElement(name = "meetup")
@XmlAccessorType(XmlAccessType.NONE)
public class Meetup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true)
	private String uuid;

	@Column(name="TITLE")
	@XmlElement
	private String title;

	@Column(length=500,name="DESCRIPTION")
	@XmlElement
	private String description;

	@Embedded
	@Column(name="LOCATION")
	@XmlElement
	private Location location;

	@Column(name="START_DT")
	private Date startDate;

	@Column(name="END_DT")
	private Date endDate;

	@OneToOne
	private User organizer;
	
	@OneToMany(mappedBy="meetup",fetch=FetchType.EAGER)
	@Cascade(value=CascadeType.ALL)
	private Set<MeetupAttendee> attendees = new HashSet<>();
	
	@OneToMany(mappedBy="meetup",fetch=FetchType.EAGER)
	@Cascade(value=CascadeType.ALL)
	private Set<MeetupMessage> messages = new HashSet<>();
	
	@OneToMany(mappedBy="meetup",fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.ALL)
	@JsonIgnore
	private Set<MeetupAddressInfo> addressComponents = new HashSet<>();

	@OneToOne
	@JoinColumn(name="EVENT_ID")
	private Event eventAtMeetup;
	
	public Event getEventAtMeetup() {
		return eventAtMeetup;
	}

	public void setEventAtMeetup(Event eventAtMeetup) {
		this.eventAtMeetup = eventAtMeetup;
	}

	@JsonIgnore
	public Set<MeetupAddressInfo> getAddressComponents() {
		return addressComponents;
	}

	@JsonProperty
	public void setAddressComponents(Set<MeetupAddressInfo> addressComponents) {
		this.addressComponents = addressComponents;
	}

	public Set<MeetupMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<MeetupMessage> messages) {
		this.messages = messages;
	}

	public Set<MeetupAttendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<MeetupAttendee> attendees) {
		this.attendees = attendees;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


}
