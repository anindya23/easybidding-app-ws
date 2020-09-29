package com.easybidding.app.ws.shared.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CountyDto {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String countyCode;
	private String countyName;
	private StateDto state;
	private CountryDto country;
	private String status;
//	private UserDto createdBy;
//	private UserDto lastUpdatedBy;
	private String dateCreated;
	private String dateLastUpdated;

	public String getCountyCode() {
		return countyCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
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

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateLastUpdated(String dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
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

}
