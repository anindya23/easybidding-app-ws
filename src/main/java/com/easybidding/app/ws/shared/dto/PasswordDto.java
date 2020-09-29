package com.easybidding.app.ws.shared.dto;

import com.easybidding.app.ws.validation.ChangePasswordMatches;
import com.easybidding.app.ws.validation.ValidPassword;

@ChangePasswordMatches
public class PasswordDto {

	private String id;

	private String email;
	
	private String token;
	
	private String oldPassword;

	@ValidPassword
	private String password;

	private String matchingPassword;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

}
