package com.easybidding.app.ws.shared.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserProfileDto {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String district;
	private String postCode;
	private String telephone;
	private String mobile;
	private CountyDto county;
	private StateDto state;
	private CountryDto country;
	private String status;
	private UserProfileDto createdBy;
	private UserProfileDto lastUpdatedBy;
	private String dateCreated;
	private String dateLastUpdated;
	private String dateLastActive;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public UserProfileDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserProfileDto createdBy) {
		this.createdBy = createdBy;
	}

	public UserProfileDto getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserProfileDto lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateLastUpdated(String dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public void setDateLastActive(String dateLastActive) {
		this.dateLastActive = dateLastActive;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateCreated = dateFormat.format(dateCreated);
	}

	public String getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateLastUpdated = dateFormat.format(dateLastUpdated);
	}

	public String getDateLastActive() {
		return dateLastActive;
	}

	public void setDateLastActive(Date dateLastActive, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateLastActive = dateFormat.format(dateLastActive);
	}

}
