package com.easybidding.app.ws.shared.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AccountDto {

	private String id;
	private String accountName;
	private String accountDescription;
	private String address;
	private String city;
	private String district;
	private String postCode;
	private String email;
	private String telephone;
	private String mobile;
	private String website;
	private CountyDto county;
	private StateDto state;
	private CountryDto country;
	private String status;
	private UserDto createdBy;
	private UserDto lastUpdatedBy;
	private Date dateCreated;
	private Date dateLastUpdated;
	private Date dateLastActive;
	private Set<JobDto> jobs = new HashSet<JobDto>();
//	private List<AccountRoleDto> roles;
//	private List<AccountInitParamDto> accountParams;
//	private List<UserDto> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public CountyDto getCounty() {
		return county;
	}

	public void setCounty(CountyDto county) {
		this.county = county;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto country) {
		this.country = country;
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

	public void addJob(JobDto job) {
		this.jobs.add(job);
		job.getAccounts().add(this);
	}

	public void removeJob(JobDto job) {
		this.jobs.remove(job);
		job.getAccounts().remove(this);
	}

	public Set<JobDto> getJobs() {
		return jobs;
	}

	public void setJobs(Set<JobDto> jobs) {
		this.jobs = jobs;
	}
//
//	public List<AccountRoleDto> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(List<AccountRoleDto> roles) {
//		this.roles = roles;
//	}
//
//	public List<AccountInitParamDto> getAccountParams() {
//		return accountParams;
//	}
//
//	public void setAccountParams(List<AccountInitParamDto> accountParams) {
//		this.accountParams = accountParams;
//	}
//
//	public List<UserDto> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<UserDto> users) {
//		this.users = users;
//	}

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

	public Date getDateLastActive() {
		return dateLastActive;
	}

	public void setDateLastActive(Date dateLastActive) {
		this.dateLastActive = dateLastActive;
	}

}
