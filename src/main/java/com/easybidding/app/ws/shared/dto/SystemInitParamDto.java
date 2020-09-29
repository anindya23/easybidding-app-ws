package com.easybidding.app.ws.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class SystemInitParamDto implements Serializable {

	private static final long serialVersionUID = 7283681128293666538L;

	private String id;
	private String paramKey;
	private String paramValue;
	private String defaultValue;
	private String status;
	private UserDto createdBy;
	private Date dateCreated;
	private UserDto lastUpdatedBy;
	private Date dateLastUpdated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDto createdBy) {
		this.createdBy = createdBy;
	}

	public UserDto getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserDto lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

}
