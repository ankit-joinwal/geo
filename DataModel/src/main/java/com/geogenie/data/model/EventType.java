package com.geogenie.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "EVENT_TYPE")
@XmlRootElement(name="eventType")
public class EventType {

	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	private Long id;

	@Column
	@XmlElement
	@JsonProperty
	private String name;

	@Column
	@XmlElement
	@JsonProperty
	private String description;

	@JsonIgnore
	@XmlTransient
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "CATEGORY_EVENT_TYPE", joinColumns = { @JoinColumn(name = "EVENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID") })
	private Set<Category> relatedCategories = new HashSet<>();
	
	@JsonProperty
	@ManyToMany(mappedBy="relatedEventTypes",fetch=FetchType.EAGER)
	private Set<EventTag> relatedTags = new HashSet<>();
	
	
	
	
	public Set<EventTag> getRelatedTags() {
		return relatedTags;
	}

	public void setRelatedTags(Set<EventTag> relatedTags) {
		this.relatedTags = relatedTags;
	}

	@JsonIgnore
	public Set<Category> getRelatedCategories() {
		return relatedCategories;
	}

	@JsonProperty
	public void setRelatedCategories(Set<Category> relatedCategories) {
		this.relatedCategories = relatedCategories;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "EventType [name = " + name + " ]";
	}
}
