package com.geogenie.data.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ankit.Joinwal
 * 
 */
@Entity
@Table(name = "userDetails")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)

@NamedQuery(name = "getUserByEmail", query = "from User where emailId like :emailId")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	private Long id;
	@XmlElement
	@Column(nullable = false)
	@NotNull(message="error.name.mandatory")
	private String name;
	
	@XmlElement
	@Column(nullable = false)
	@NotNull(message="error.email.mandatory")
	private String emailId;
	@Column(nullable = false)
	@NotNull(message="error.password.mandatory")
	@XmlTransient
	@JsonIgnore
	private String password;
	
	@Column(nullable=false)
	@XmlTransient
	@JsonIgnore
	private String isEnabled;
	
	@XmlTransient
	@Column(nullable = false)
	@JsonIgnore
	private Date createDt;
	
	@XmlElement
	@Column(name="fb_id")
	private String fbId;
	
	@XmlTransient
	@Column(nullable=false)
	@JsonIgnore
	private Integer dailyQuota;
	
	@XmlElement(name="devices")
	@JsonProperty
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "userDevice", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "device_id") })
	Set<SmartDevice> smartDevices = new HashSet<>();
	
	public Set<SmartDevice> getSmartDevices() {
		return smartDevices;
	}

	public void setSmartDevices(Set<SmartDevice> smartDevices) {
		this.smartDevices = smartDevices;
	}

	@JsonIgnore
	public String getIsEnabled() {
		return isEnabled;
	}

	@JsonProperty
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	@JsonIgnore
	public Integer getDailyQuota() {
		return dailyQuota;
	}

	@JsonProperty
	public void setDailyQuota(Integer dailyQuota) {
		this.dailyQuota = dailyQuota;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	@JsonProperty
	public void setId(Long id) {
		this.id = id;
	}

	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	@JsonIgnore
	public Date getCreateDt() {
		return createDt;
	}

	@JsonProperty
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	@Override
	public String toString() {
		return "[ name = " + name + " , email = " + emailId + " , fb_id = "+this.fbId+" ] ";
	}

}
