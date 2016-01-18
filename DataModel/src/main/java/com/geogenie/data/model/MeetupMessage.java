package com.geogenie.data.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MEETUP_MESSAGES")
@XmlRootElement
public class MeetupMessage implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	@JsonProperty(value="message")
	private String message;

	@OneToOne
	@JoinColumn(name = "attendee_id")
	@XmlTransient
	@JsonIgnore
	private MeetupAttendee meetupAttendee;

	 @ManyToOne
	 @JsonIgnore
	 @XmlTransient
	 @JoinColumn(name="meetup_id")
	 private Meetup meetup;
	 
	
	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MeetupAttendee getMeetupAttendee() {
		return meetupAttendee;
	}

	public void setMeetupAttendee(MeetupAttendee meetupAttendee) {
		this.meetupAttendee = meetupAttendee;
	}
	@Override
	public String toString() {
		return "Message ="+this.message;
	}
	
}
