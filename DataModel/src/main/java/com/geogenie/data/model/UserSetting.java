package com.geogenie.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER_SETTINGS")
@XmlRootElement(name = "settings")
public class UserSetting {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(nullable=false,name="SETTING_TYPE")
	private String settingType;

	@Column(nullable=false,name="NAME")
	private String name;

	@Column(nullable=false,name="DISPLAY_NAME")
	private String displayName;
	
	@Column(nullable=false,name="VALUE")
	private String value;

	@ManyToOne
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(nullable=false,name="CREATE_DT")
	@JsonIgnore
	private Date createDt;
	
	
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSettingType() {
		return settingType;
	}

	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Setting [name=" + this.name + " ,type=" + this.settingType
				+ " ,value=" + this.value + " ]";
	}

}
