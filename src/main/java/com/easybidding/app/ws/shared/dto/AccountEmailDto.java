package com.easybidding.app.ws.shared.dto;

import com.easybidding.app.ws.validation.ValidEmail;

public class AccountEmailDto {

	@ValidEmail
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
