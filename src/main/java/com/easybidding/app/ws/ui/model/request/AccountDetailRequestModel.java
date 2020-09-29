package com.easybidding.app.ws.ui.model.request;

public class AccountDetailRequestModel {

	private String id;
	private String accountName;
	private String address;
	private String city;
	private String district;
	private String postCode;
	private String telephone;
	private String email;
	private String mobile;
	private String website;
	private String status;
	private CountyRequestModel county;
	private StateRequestModel state;
	private CountryRequestModel country;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CountyRequestModel getCounty() {
		return county;
	}

	public void setCounty(CountyRequestModel county) {
		this.county = county;
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

}
