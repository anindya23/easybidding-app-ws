package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.config.TokenProvider;
import com.easybidding.app.ws.event.OnPasswordChangeEvent;
import com.easybidding.app.ws.io.entity.AuthToken;
import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.repository.impl.UserRepository;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.PasswordDto;
import com.easybidding.app.ws.shared.dto.UserDto;
import com.easybidding.app.ws.shared.dto.UserEmailDto;
import com.easybidding.app.ws.ui.model.request.UserLoginRequestModel;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider jwtTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<AuthToken> login(@RequestBody UserLoginRequestModel loginUser) throws AuthenticationException {

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(authentication);
		return ResponseEntity.ok(new AuthToken(token));
	}

	@PostMapping("/forgot/password")
	public OperationStatusModel sendPasswordToken(@Valid @RequestBody UserEmailDto request, final HttpServletRequest httpRequest) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.FORGOTPASSWORD.name());

		UserDto user = userService.getUserByEmail(request.getEmail());

		if (user == null)
			throw new UsernameNotFoundException("User not found with email: " + request.getEmail());

		eventPublisher
				.publishEvent(new OnPasswordChangeEvent(user, httpRequest.getLocale(), utils.getAppUrl(httpRequest)));

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@Transactional
	@PostMapping("/reset/password")
	public OperationStatusModel resetPassword(@Valid @RequestBody PasswordDto request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.RESETPASSWORD.name());

		String status = userService.validateVerificationToken(request.getToken());

		if (status.equals("invalidToken"))
			throw new RuntimeException("Invalid token: " + request.getToken());

		if (status.equals("expired"))
			throw new RuntimeException("Token Expired: " + request.getToken());

		UserEntity user = userService.getUserByToken(request.getToken());
		userService.changeUserPassword(user, request.getPassword());
		userService.removeToken(request.getToken());

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@Transactional
	@PostMapping("/activate")
	public OperationStatusModel activateUser(@Valid @RequestBody PasswordDto request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.ACTIVE.name());

		String status = userService.validateVerificationToken(request.getToken());

		if (status.equals("invalidToken"))
			throw new RuntimeException("Invalid token: " + request.getToken());

		if (status.equals("expired"))
			throw new RuntimeException("Token Expired: " + request.getToken());

		UserEntity user = userService.getUserByToken(request.getToken());
		userService.changeUserPassword(user, request.getPassword());
		userService.removeToken(request.getToken());

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

//	@PreAuthorize("hasAnyRole('SYS_ADMIN', 'ACC_ADMIN')")
//	@PostMapping("/register")
//	public OperationStatusModel createUser(@Valid @RequestBody UserDto request, final HttpServletRequest httpRequest)
//			throws ParseException {
//		OperationStatusModel response = new OperationStatusModel();
//		response.setOperationName(RequestOperationName.CREATE.name());
//
//		UserEntity registered = userService.register(request);
//		eventPublisher.publishEvent(
//				new OnRegistrationCompleteEvent(registered, httpRequest.getLocale(), utils.getAppUrl(httpRequest)));
//
//		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
//		return response;
//	}

//	@GetMapping("/activate/{token}")
//	public OperationStatusModel activateUser(@PathVariable String token, HttpServletRequest req,
//			HttpServletResponse res) {
//		OperationStatusModel response = new OperationStatusModel();
//		response.setOperationName(RequestOperationName.ACTIVE.name());
//
//		String status = userService.validateVerificationToken(token);
//
//		if (status.equals("invalidToken"))
//			throw new RuntimeException("Invalid token: " + token);
//
//		if (status.equals("expired"))
//			throw new RuntimeException("Token Expired: " + token);
//
//		UserEntity user = userService.getUserByToken(token);
////		userService.activateUser(user, res);
//		userService.activateUser(user);
//
//		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
//		return response;
//	}
//
//	@GetMapping("/user/logout")
//	public OperationStatusModel logout (final HttpServletRequest req, final HttpServletResponse res) {
//		OperationStatusModel response = new OperationStatusModel();
//		response.setOperationName(RequestOperationName.LOGOUT.name());
//		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		
//		if (auth != null)
//			new SecurityContextLogoutHandler().logout(req, res, auth);
//
//		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
//		return response;
//	}

}
