package com.easybidding.app.ws.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.easybidding.app.ws.event.OnRegistrationCompleteEvent;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.dto.UserDto;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
		final UserDto user = event.getUser();
		final String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(user, token);

		final SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}

	private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final UserDto user,
			final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
//		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
		final String confirmationUrl = "http://localhost:3000/reset-password.html?token=" + token;
		final String message = messages.getMessage("message.regSuccLink", null,
				"You registered successfully. To confirm your registration, please click on the below link.",
				event.getLocale());
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

}