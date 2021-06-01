package com.easybidding.app.ws.config;

import com.easybidding.app.ws.EasyBiddingApplicationContext;

public class SecurityConstant {

	public static final long EXPIRATION_TIME = 18000000;
	public static final String TOKEN_PREFIX = "Eb_Bearer ";
	public static final String HEADER_STRING = "Authorization";
//	public static final String SIGN_UP_URL = "/api/v1/auth/register";
	public static final String REG_ACTIVATION_URL = "/api/v1/auth/activate/{token}";
	public static final String FORGOT_PASSWORD_URL = "/api/v1/auth/forgot/password";
	public static final String RESET_PASSWORD_URL = "/api/v1/auth/reset/password";
	public static final String LOGIN_URL = "/api/v1/auth/login";

	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) EasyBiddingApplicationContext.getBean("appProperties");
		return appProperties.getTokenSecret();
	}
}
