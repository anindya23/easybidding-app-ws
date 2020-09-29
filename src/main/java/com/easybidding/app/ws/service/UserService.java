package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	String validateVerificationToken(String token);

	boolean checkIfValidOldPassword(final UserEntity user, final String oldPassword);

	UserDto getUserById(String id);

	UserEntity getUserByToken(String token);

	UserDto getUserByEmail(String email);
	
	UserEntity getByEmail(String email);

	List<UserDto> getAllUsers();

	List<UserDto> getAllUsersByAccount(String accountId);

	List<UserDto> getAllUsersByAccountAndStatus(String accountId, String status);

	Map<String, Object> getUsers(int page, int size);

	Map<String, Object> getUsersByAccount(String accountId, int page, int size);

	Map<String, Object> getUsersByAccountAndStatus(String accountId, String status, int page, int size);

	UserEntity register(UserDto dto);

	UserDto save(UserDto dto);

	void deleteUser(String id);

	void changeUserPassword(final UserEntity user, final String password);

	void createVerificationTokenForUser(UserEntity user, String token);

	void activateUser(UserEntity user, final HttpServletResponse res);

	void batchSave(List<UserDto> dtos);

	void batchDelete(List<String> ids);
}
