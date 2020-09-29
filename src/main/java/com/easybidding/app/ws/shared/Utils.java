package com.easybidding.app.ws.shared;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String generateUniqueId(int length) {
		return generatedRandomString(length);
	}

	private String generatedRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}

	public String getIpAddress() {
		return "127.0.0.1";
	}

	public boolean isInteger(Object object) {
		if (object instanceof Integer) {
			return true;
		} else {
			String string = object.toString();
			try {
				Integer.parseInt(string);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	public Date convertStringToDate(String pattern, String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		return formatter.parse(date);
	}
	
	public String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	public String getClientIP(HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

}
