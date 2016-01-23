package com.geogenie.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EVENT_IMAGES")
public class EventImage {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private byte[] data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "EventImage [name="+this.name+" ]";
	}
}
