package com.geogenie.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name="smartDevice")
@XmlRootElement(name="smartDevice")
@XmlAccessorType(XmlAccessType.NONE)
public class SmartDevice implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	private Long id;
	@Column(nullable=false)
	@XmlElement
	@NotNull(message="error.uniqueid.mandatory")
	private String uniqueId;
	
	@Column(nullable=false)
	@XmlElement
	@NotNull(message="error.build.version.mandatory")
	private String buildVersion;
	@Column(nullable=false)
	@XmlElement
	@NotNull(message="error.osversion.mandatory")
	private String osVersion;
	@Column(nullable=false)
	@XmlElement
	@NotNull(message="error.device.type.mandatory")
	private DeviceType deviceType;
	@Column(nullable=false)
	@XmlTransient
	@JsonIgnore
	private String isEnabled;
	@XmlTransient
	@JsonIgnore
	@Column(nullable=false)
	private Date createDt;
	
	
	@JsonIgnore
	public Long getId() {
		return id;
	}

	@JsonProperty
	public void setId(Long id) {
		this.id = id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}



	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
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
	public Date getCreateDt() {
		return createDt;
	}

	@JsonProperty
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	

	@Override
	public String toString() {
		return "SmartDevice : [ uniqueId = "  + uniqueId+ " , " +
				"buildVersion = " + buildVersion+ " , " +
				"osVersion = "  + osVersion + " , " +
				"deviceType = " + deviceType.toString() + " , " +
				"isEnabled = " + isEnabled +" , " +
				"]";
	}
	
	
}
