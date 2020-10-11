package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.repository.impl.UserRepository;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.dto.PasswordDto;
import com.easybidding.app.ws.shared.dto.UserDetailDto;
import com.easybidding.app.ws.shared.dto.UserDto;
import com.easybidding.app.ws.shared.dto.UserEmailDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/users")
public class UsersController {

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper mapper;

	@GetMapping("/user/{id}")
	public UserDto getUser(@PathVariable String id) {
		return userService.getUserById(id);
	}

	@GetMapping("/user/{email}")
	public UserDto getUserByEmail(@PathVariable String email) {
		return userService.getUserByEmail(email);
	}

	@GetMapping("/account/{accountId}")
	public List<UserDto> getAllUsersByAccount(@PathVariable String accountId) {
		return userService.getAllUsersByAccount(accountId);
	}

	@GetMapping("/account/{accountId}/status/{status}")
	public List<UserDto> getAllUsersByAccountAndStatus(@PathVariable String accountId, @PathVariable String status) {
		return userService.getAllUsersByAccountAndStatus(accountId, status);
	}

	@GetMapping("/account/{accountId}/page/{page}/size/{size}")
	public Map<String, Object> getUsersByAccount(@PathVariable String accountId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return userService.getUsersByAccount(accountId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/account/{accountId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getUsersByAccountAndStatus(@PathVariable String accountId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return userService.getUsersByAccountAndStatus(accountId, status, page, size);
	}

	@PostMapping("/user/email/search")
	public String checkEmailAvailability(@Valid @RequestBody UserDetailDto request) {
		UserEntity user = userRepository.findByEmail(request.getEmail());
		if (user != null && user.getId() != null)
			return "false";
		else
			return "true";
	}

	@PutMapping("/user")
	public UserDetailDto updateUser(@Valid @RequestBody UserDetailDto request) {
		UserEntity user = userRepository.getOne(request.getId());

		UserEntity entity = null;

		if (!user.getEmail().equals(request.getEmail()))
			entity = userRepository.findByEmail(request.getEmail());

		if (entity != null) {
			throw new RuntimeException("Email is not available");
		}

		UserDto dto = mapper.map(request, UserDto.class);
		UserDto updatedDto = userService.save(dto);
		return mapper.map(updatedDto, UserDetailDto.class);
	}

	@PostMapping("/user/change/password")
	public OperationStatusModel changeUserPassword(@Valid @RequestBody PasswordDto request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.UPDATEPASSWORD.name());

		UserEntity user = null;

		if (request.getId() != null)
			user = userRepository.getOne(request.getId());

		if (request.getEmail() != null)
			user = userRepository.findByEmail(request.getEmail());

		if (user == null)
			throw new UsernameNotFoundException("User Not Found");

		userService.changeUserPassword(user, request.getPassword());

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping("/user/{id}")
	public OperationStatusModel deleteUser(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	/*
	 * Refactoring 1. Need to check all emails are unique
	 */
	@PostMapping
	public OperationStatusModel batchsave(@Valid @RequestBody List<UserDetailDto> request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		List<UserDto> dtos = new ArrayList<UserDto>();

		for (UserDetailDto dto : request)
			dtos.add(mapper.map(dto, UserDto.class));

		userService.batchSave(dtos);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	/*
	 * Refactoring 1. Need to check all ids are valid
	 */
	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<UserDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (UserDto req : request) {
			ids.add(req.getId());
		}

		userService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
