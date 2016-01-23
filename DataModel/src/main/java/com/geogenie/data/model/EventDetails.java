package com.geogenie.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT_DETAILS")
@XmlRootElement(name="event_details")
public class EventDetails {

	@Id
	@GeneratedValue()
	@XmlTransient
	@JsonIgnore
	private Long id;
	
	@Embedded
	@Column
	@XmlElement
	private Location location;

	@OneToOne
	@Cascade(value = CascadeType.ALL)
	private User organizer;
	
	@XmlTransient
	@JsonIgnore
	@Column(nullable = false)
	private Date createDt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	@Override
	public String toString() {
		return "EventDetails [location="+this.location+ " , organizer="+this.organizer+" ]";
	}

}
