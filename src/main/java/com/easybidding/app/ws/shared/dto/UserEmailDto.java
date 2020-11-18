package com.easybidding.app.ws.shared.dto;

import com.easybidding.app.ws.validation.ValidEmail;

public class UserEmailDto {

	private String userId;

	@ValidEmail
	private String email;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
