package com.easybidding.app.ws.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.easybidding.app.ws.event.OnPasswordChangeEvent;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.dto.UserDto;

@Component
@PropertySource("classpath:application.properties")
public class PasswordChangeListener implements ApplicationListener<OnPasswordChangeEvent> {

	@Value("${support.email}")
	private String supportEmail;
	
	@Value("${email.web.server}")
	private String webServer;
	
	@Autowired
	private UserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

//	@Autowired
//	private Environment env;

	@Override
	public void onApplicationEvent(final OnPasswordChangeEvent event) {
		this.confirmPassword(event);
	}

	private void confirmPassword(final OnPasswordChangeEvent event) {
		final UserDto user = event.getUser();
		final String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(user, token);

		final SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}

	private SimpleMailMessage constructEmailMessage(final OnPasswordChangeEvent event, final UserDto user,
			final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Password Change Confirmation";
		final String confirmationUrl = webServer + "/reset-password.html?token=" + token;
//		final String confirmationUrl = "http://localhost:3000/reset-password.html?token=" + token;
		final String message = messages.getMessage("message.changePassLink", null,
				"To reset password, please click on the below link.", event.getLocale());
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
//		email.setFrom(env.getProperty("support.email"));
		email.setFrom(supportEmail);
		return email;
	}

}