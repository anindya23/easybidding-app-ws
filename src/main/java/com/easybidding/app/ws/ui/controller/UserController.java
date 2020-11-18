package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.exception.InvalidOldPasswordException;
import com.easybidding.app.ws.exception.InvalidUsernameException;
import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.repository.impl.UserRepository;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.dto.PasswordDto;
import com.easybidding.app.ws.shared.dto.UserDto;
import com.easybidding.app.ws.shared.dto.UserEmailDto;
import com.easybidding.app.ws.shared.dto.UserProfileDto;
import com.easybidding.app.ws.ui.model.request.UserLoginRequestModel;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper mapper;

	@GetMapping
	public UserDto getProfile() {
		return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	@PostMapping("/validate/credentials")
	public String validateCredentials(@Valid @RequestBody UserLoginRequestModel request) {
		UserEntity user = userRepository.findByEmail(request.getEmail());

		if (user instanceof UserEntity && userService.checkIfValidOldPassword(user, request.getPassword()))
			return "true";
		else
			return "false";
	}

	@PutMapping("/update/profile")
	public UserProfileDto updateProfile(@Valid @RequestBody UserProfileDto request) {
		UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!request.getEmail().equals(user.getEmail()))
			throw new RuntimeException("You're not logged in with the same profile you wanted to update");

		request.setId(user.getId());
		UserDto dto = mapper.map(request, UserDto.class);
		UserDto updatedDto = userService.save(dto);
		return mapper.map(updatedDto, UserProfileDto.class);
	}

	@PutMapping("/update/password")
	public OperationStatusModel changeUserPassword(@Valid @RequestBody PasswordDto request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.UPDATEPASSWORD.name());

		UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userService.checkIfValidOldPassword(user, request.getOldPassword()))
			throw new InvalidOldPasswordException("Old Password is not valid");

		userService.changeUserPassword(user, request.getPassword());

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PutMapping("/update/username")
	public OperationStatusModel changeUsername(@Valid @RequestBody UserEmailDto request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.UPDATEUSERNAME.name());

		if (request.getEmail() == SecurityContextHolder.getContext().getAuthentication().getName()) {
			throw new InvalidUsernameException("You're not logged in with the same Username you wanted to change");
		}
		
		UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		userService.changeUsername(user, request.getEmail());

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
