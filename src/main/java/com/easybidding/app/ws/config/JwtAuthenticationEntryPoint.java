package com.easybidding.app.ws.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6698900147201554333L;

	@Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
    }

//	@Override
//	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
//			throws IOException, ServletException {
//		res.setContentType("application/json;charset=UTF-8");
//		res.setStatus(HttpServletResponse.SC_FORBIDDEN);
//		res.getWriter().write(new JSONObject() 
//                .put("timestamp", LocalDateTime.now())
//                .put("message", "Access denied")
//                .toString());
//	}
}
