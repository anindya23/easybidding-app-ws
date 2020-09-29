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
@Table(name = "eb_states")
@ApiModel(description = "All state list.")
public class StateEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4816649861763841442L;

	public enum Status {
		Active, Inactive, Deleted,
	}

	@Column(length = 10)
	private String stateCode;

	@Column(nullable = false, length = 255)
	private String stateName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private CountryEntity country;

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

//	@OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//	private List<CountyEntity> counties = new ArrayList<CountyEntity>();

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
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

//	public List<CountyEntity> getCounties() {
//		return counties;
//	}
//
//	public void addCounty(CountyEntity county) {
//		counties.add(county);
//		county.setState(this);
//	}
//
//	public void setCounties(List<CountyEntity> counties) {
//		this.counties = counties;
//	}

}
