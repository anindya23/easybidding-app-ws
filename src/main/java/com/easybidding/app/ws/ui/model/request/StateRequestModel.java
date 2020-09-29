package com.easybidding.app.ws.ui.model.request;

import java.util.List;

public class StateRequestModel {

	private String id;
	private String stateCode;
	private String stateName;
	private CountryRequestModel country;
	private String status;
	private List<CountyRequestModel> counties;

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

	public List<CountyRequestModel> getCounties() {
		return counties;
	}

	public void setCounties(List<CountyRequestModel> counties) {
		this.counties = counties;
	}

}
