package com.easybidding.app.ws.ui.model.request;

import java.util.List;

public class CountryRequestModel {

	private String id;
	private String countryCode;
	private String countryName;
	private String status;
	private List<StateRequestModel> states;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<StateRequestModel> getStates() {
		return states;
	}

	public void setStates(List<StateRequestModel> states) {
		this.states = states;
	}

}
