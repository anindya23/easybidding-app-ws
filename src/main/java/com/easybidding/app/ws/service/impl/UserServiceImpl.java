package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.exception.UserAlreadyExistException;
import com.easybidding.app.ws.io.entity.AccountEntity;
import com.easybidding.app.ws.io.entity.RoleEntity;
import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.io.entity.UserEntity.Status;
import com.easybidding.app.ws.io.entity.VerificationTokenEntity;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.RoleRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.repository.impl.UserRepository;
import com.easybidding.app.ws.repository.impl.VerificationTokenRepository;
import com.easybidding.app.ws.service.UserService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.shared.dto.RoleDto;
import com.easybidding.app.ws.shared.dto.UserDto;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	VerificationTokenRepository tokenRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	private ModelMapper mapper;

	@Autowired
	public UserServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<UserDto, UserEntity> entityMapping = new PropertyMap<UserDto, UserEntity>() {
		protected void configure() {
			skip().setAccount(null);
			skip().setCountry(null);
			skip().setState(null);
			skip().setRoles(null);
			skip().setPassword(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
			skip().setDateLastActive(null);
		}
	};

	PropertyMap<UserEntity, UserDto> dtoMapping = new PropertyMap<UserEntity, UserDto>() {
		protected void configure() {
			skip().setAccount(null);
			skip().setRoles(null);
			skip().setPassword(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
			skip().setDateLastActive(null);
		}
	};

	@Override
	public UserDto getUserById(String id) {
		UserEntity entity = userRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("User doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		UserEntity entity = userRepository.findByEmail(email);

		if (entity == null)
			throw new RuntimeException("User doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public UserEntity getByEmail(String email) {
		UserEntity entity = userRepository.findByEmail(email);

		if (entity == null)
			throw new RuntimeException("User doesn't exist");

		return entity;
	}

	@Override
	public UserEntity getUserByToken(String token) {
		final VerificationTokenEntity entity = tokenRepository.findByToken(token);

		if (entity != null)
			return entity.getUser();

		return null;
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<UserEntity> entities = userRepository.findAll();

		if (entities == null) {
			throw new RuntimeException("No User found");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<UserDto> getAllUsersByAccount(String accountId) {
		List<UserEntity> entities = userRepository.findAllUsersByAccount(accountId);

		if (entities == null) {
			throw new RuntimeException("No User found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<UserDto> getAllUsersByAccountAndStatus(String accountId, String status) {
		Status enumStatus = UserEntity.Status.valueOf(status);
		List<UserEntity> entities = userRepository.findAllUsersByAccountAndStatus(accountId, enumStatus);

		if (entities == null) {
			throw new RuntimeException("No User found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<UserEntity> entities = userRepository.findAll(pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No User found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getUsersByAccount(String accountId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<UserEntity> entities = userRepository.findUsersByAccount(accountId, pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No User found under this account");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getUsersByAccountAndStatus(String accountId, String status, int page, int size) {
		Status enumStatus = UserEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);

		Page<UserEntity> entities = userRepository.findUsersByAccountAndStatus(accountId, enumStatus, pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No User found under this account");

		return finalizePageResponse(entities);
	}

	@Override
	@Transactional
	public UserEntity register(UserDto dto) throws UserAlreadyExistException {
		if (emailExists(dto.getEmail()))
			throw new UserAlreadyExistException("There is an account with that email adress: " + dto.getEmail());

		UserEntity entity = convertDtoToEntity(dto, null);
		entity.setStatus(UserEntity.Status.INACTIVE);

		UserEntity savedEntity = userRepository.save(entity);
		return savedEntity;
	}

	@Override
	@Transactional
	public UserDto save(UserDto dto) {
		UserEntity entity = null;

		if (dto.getId() != null) {
			entity = userRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No User found with ID: " + dto.getId());
		}

		UserEntity savedEntity = userRepository.save(convertDtoToEntity(dto, entity));
		return this.mapper.map(savedEntity, UserDto.class);
	}

	@Override
	public void softDeleteUser(String id) {
		UserEntity entity = userRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("No User found");
		
		entity.setStatus(Status.DELETED);
		userRepository.save(entity);
	}

	@Override
	public void batchSave(List<UserDto> dtos) {
		Set<UserEntity> entities = new HashSet<UserEntity>();

		for (UserDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, userRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		userRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		userRepository.deleteByIdIn(ids);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		boolean enabled = false;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		UserEntity entity = userRepository.findByEmail(email);

		if (entity == null)
			throw new UsernameNotFoundException("User not found with email: " + email);

		if (entity.getStatus() == UserEntity.Status.ACTIVE)
			enabled = true;

		return new User(entity.getEmail(), entity.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, getAuthority(entity));
	}

	private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
		});
		return authorities;

	}

	@Override
	public void activateUser(UserEntity user) {
		user.setStatus(Status.ACTIVE);
		userRepository.save(user);
	}

	@Override
	public void createVerificationTokenForUser(final UserDto user, final String token) {
		final VerificationTokenEntity myToken = new VerificationTokenEntity(token, convertDtoToEntity(user, null));
		tokenRepository.save(myToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		final VerificationTokenEntity entity = tokenRepository.findByToken(token);
		if (entity == null) {
			return TOKEN_INVALID;
		}

		final UserEntity user = entity.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((entity.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			tokenRepository.delete(entity);
			return TOKEN_EXPIRED;
		}

		user.setStatus(Status.ACTIVE);
		userRepository.save(user);
		return TOKEN_VALID;
	}

	@Override
	public boolean checkIfValidOldPassword(final UserEntity user, final String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	@Override
	public void changeUserPassword(final UserEntity user, final String password) {
		user.setPassword(passwordEncoder.encode(password));
		user.setStatus(Status.ACTIVE);
		userRepository.save(user);
	}

	@Override
	public void changeUsername(UserEntity user, String userName) {
		user.setEmail(userName);
		userRepository.save(user);
	}

	private boolean emailExists(String email) {
		return userRepository.findByEmail(email) != null;
	}

	@Override
	public void removeToken(String token) {
		VerificationTokenEntity entity = tokenRepository.findByToken(token);

		if (entity == null)
			throw new RuntimeException("No Token found");

		tokenRepository.delete(entity);
	}

	private UserEntity convertDtoToEntity(UserDto dto, UserEntity entity) {
		if (dto.getId() == null) {
			entity = this.mapper.map(dto, UserEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			entity = userRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No User found");

			this.mapper.map(dto, entity);
		}

		if (dto.getAccount() != null) {
			AccountEntity account = null;

			if (dto.getAccount().getId() != null) {
				account = accountRepository.getOne(dto.getAccount().getId());
			}

			if (account == null && dto.getAccount().getEmail() != null) {
				account = accountRepository.findByEmail(dto.getAccount().getEmail());
			}

			if (account == null)
				throw new RuntimeException("No Account found");

			entity.setAccount(account);
		}

		if (dto.getRoles() != null) {
			Set<RoleEntity> entities = new HashSet<RoleEntity>();

			for (RoleDto role : dto.getRoles()) {
				if (role.getRoleCode() != null) {
					entities.add(roleRepository.findByRoleCode(role.getRoleCode()));
				}
			}
			entity.setRoles(entities);
		} else {
			Set<RoleEntity> entities = new HashSet<RoleEntity>();
			entities.add(roleRepository.findByRoleCode("SYS_ADMIN"));
			entity.setRoles(entities );
		}

		if (dto.getPassword() != null) {
			entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		if (dto.getCountry() != null) {
			entity.setCountry(countryRepository.findByCountryCode(dto.getCountry().getCountryCode()));
		}

		if (dto.getState() != null && dto.getCountry() != null) {
			entity.setState(
					stateRepository.findByStateCode(dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		return entity;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private UserDto convertEntityToDto(UserEntity entity) {
		UserDto response = this.mapper.map(entity, UserDto.class);

		if (entity.getAccount() != null)
			response.setAccount(this.mapper.map(entity.getAccount(), AccountDto.class));

		if (entity.getRoles() != null)
			response.setRoles(convertRolesToDto(entity.getRoles()));

		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		response.setDateLastActive(entity.getDateLastActive(), "Asia/Dhaka");
		return response;
	}

	private Set<RoleDto> convertRolesToDto(Set<RoleEntity> entities) {
		Set<RoleDto> dtos = new HashSet<RoleDto>();
		for (RoleEntity entity : entities) {
			dtos.add(this.mapper.map(entity, RoleDto.class));
		}
		return dtos;
	}

	private List<UserDto> getDtosFromEntities(List<UserEntity> entities) {
		List<UserDto> dtos = new ArrayList<UserDto>();

		for (UserEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

	private Map<String, Object> finalizePageResponse(Page<UserEntity> response) {
		List<UserEntity> entities = response.getContent();
		List<UserDto> dtos = getDtosFromEntities(entities);

		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put("content", dtos);
		responseModel.put("currentPage", response.getNumber());
		responseModel.put("totalItems", response.getTotalElements());
		responseModel.put("totalPages", response.getTotalPages());
		return responseModel;
	}

}
