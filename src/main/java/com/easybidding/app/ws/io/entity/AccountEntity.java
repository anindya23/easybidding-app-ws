package com.easybidding.app.ws.io.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "eb_accounts")
@SelectBeforeUpdate
public class AccountEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7931451271969648549L;

	public enum Status {
		ACTIVE, INACTIVE, DELETED
	}

	@Column(nullable = false, length = 255)
	private String accountName;

	@Column(columnDefinition = "TEXT")
	private String accountDescription;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(length = 255)
	private String city;

	@Column(length = 255)
	private String district;

	@Column(length = 50)
	private String postCode;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(length = 50)
	private String telephone;

	@Column(length = 50)
	private String mobile;

	@Column(length = 255)
	private String website;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "county_id")
	private CountyEntity county;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id")
	private StateEntity state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private CountryEntity country;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'DELETED')")
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_active")
	private Date dateLastActive = new Date();

//	@ManyToMany(mappedBy = "accounts", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//	private Set<JobEntity> jobs = new HashSet<JobEntity>();

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	private Set<JobAccountEntity> jobAccounts;

	@OneToMany(mappedBy = "account", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<JobFileEntity> files;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountDescription() {
		return accountDescription;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public CountyEntity getCounty() {
		return county;
	}

	public void setCounty(CountyEntity county) {
		this.county = county;
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(StateEntity state) {
		this.state = state;
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

	public Date getDateLastActive() {
		return dateLastActive;
	}

	public void setDateLastActive(Date dateLastActive) {
		this.dateLastActive = dateLastActive;
	}

	public Set<JobAccountEntity> getJobAccounts() {
		return jobAccounts;
	}

	public void setJobAccounts(Set<JobAccountEntity> jobAccounts) {
		this.jobAccounts = jobAccounts;
	}

	public List<JobFileEntity> getFiles() {
		return files;
	}

	public void setFiles(List<JobFileEntity> files) {
		this.files = files;
	}

}
