package com.easybidding.app.ws.io.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "eb_system_init_params")
@ApiModel(description = "All System Initializing Parameters")
public class SystemInitParamEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1299383613344446966L;

	public enum Status {
		Active, Inactive, Deleted,
	}

	@Column(nullable = false, length = 100)
	private String paramKey;

	@Column(nullable = false, length = 255)
	private String paramValue;

	@Column(length = 255)
	private String defaultValue;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('Active', 'Inactive', 'Deleted')")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private UserEntity createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_updated_by")
	private UserEntity lastUpdatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_updated")
	private Date dateLastUpdated = new Date();

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

}
