package com.easybidding.app.ws.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.easybidding.app.ws.shared.dto.UserDto;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private final String appUrl;
	private final Locale locale;
	private final UserDto user;

	public OnRegistrationCompleteEvent(final UserDto user, final Locale locale, final String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public UserDto getUser() {
		return user;
	}

}
