package com.easybidding.app.ws.ui.model.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StateResponseModel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String id;
	private String stateCode;
	private String stateName;
	private String status;
	private String dateCreated;
	private String dateLastUpdated;
	private CountryResponseModel country;
	private List<CountyResponseModel> counties;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public CountryResponseModel getCountry() {
		return country;
	}

	public void setCountry(CountryResponseModel country) {
		this.country = country;
	}

	public List<CountyResponseModel> getCounties() {
		return counties;
	}

	public void setCounties(List<CountyResponseModel> counties) {
		this.counties = counties;
	}

}
