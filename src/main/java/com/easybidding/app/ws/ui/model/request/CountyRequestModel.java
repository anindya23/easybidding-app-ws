package com.easybidding.app.ws.ui.model.request;

public class CountyRequestModel {

	private String id;
	private String countyCode;
	private String countyName;
	private StateRequestModel state;
	private CountryRequestModel country;
	private String status;

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

	public StateRequestModel getState() {
		return state;
	}

	public void setState(StateRequestModel state) {
		this.state = state;
	}

	public CountryRequestModel getCountry() {
		return country;
	}

	public void setCountry(CountryRequestModel country) {
		this.country = country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
