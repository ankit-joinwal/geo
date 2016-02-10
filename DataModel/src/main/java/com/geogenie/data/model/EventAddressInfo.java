package com.geogenie.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="EVENT_ADDRESS_INFO")
@XmlRootElement
public class EventAddressInfo {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private AddressComponentType addressComponentType;
	
	private String value;
	
	@ManyToOne
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "EVENT_DETAILS_ID")
	private EventDetails eventDetails;

	public EventDetails getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(EventDetails eventDetails) {
		this.eventDetails = eventDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AddressComponentType getAddressComponentType() {
		return addressComponentType;
	}

	public void setAddressComponentType(AddressComponentType addressComponentType) {
		this.addressComponentType = addressComponentType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "AddressInfo [type="+this.addressComponentType+" , value = "+this.value+" ]";
	}
	
}
