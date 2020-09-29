package com.easybidding.app.ws.ui.model.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CountyResponseModel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String countyCode;
	private String countyName;
	private String status;
	private String dateCreated;
	private String dateLastUpdated;
	private StateResponseModel state;
	private CountryResponseModel country;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountyCode() {
		return countyCode;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public StateResponseModel getState() {
		return state;
	}

	public void setState(StateResponseModel state) {
		this.state = state;
	}

	public CountryResponseModel getCountry() {
		return country;
	}

	public void setCountry(CountryResponseModel country) {
		this.country = country;
	}

}
