package com.easybidding.app.ws.ui.model.request;

public class UserDetailRequestModel {

	private String id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String district;
	private String postCode;
	private String telephone;
	private String mobile;
	private AccountDetailRequestModel account;
	private CountyRequestModel countyCode;
	private StateRequestModel stateCode;
	private CountryRequestModel countryCode;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public AccountDetailRequestModel getAccount() {
		return account;
	}

	public void setAccount(AccountDetailRequestModel account) {
		this.account = account;
	}

	public CountyRequestModel getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(CountyRequestModel countyCode) {
		this.countyCode = countyCode;
	}

	public StateRequestModel getStateCode() {
		return stateCode;
	}

	public void setStateCode(StateRequestModel stateCode) {
		this.stateCode = stateCode;
	}

	public CountryRequestModel getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(CountryRequestModel countryCode) {
		this.countryCode = countryCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
