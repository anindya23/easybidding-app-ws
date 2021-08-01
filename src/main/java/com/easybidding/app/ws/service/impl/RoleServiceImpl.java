package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.io.entity.AccountEntity;
import com.easybidding.app.ws.io.entity.RoleEntity;
import com.easybidding.app.ws.io.entity.RoleEntity.Status;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.RoleRepository;
import com.easybidding.app.ws.service.RoleService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.RoleDto;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public RoleServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<RoleDto, RoleEntity> entityMapping = new PropertyMap<RoleDto, RoleEntity>() {
		protected void configure() {
			skip().setAccount(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<RoleEntity, RoleDto> dtoMapping = new PropertyMap<RoleEntity, RoleDto>() {
		protected void configure() {
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	@Override
	public RoleDto getById(String id) {
		RoleEntity entity = roleRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("Role not found");

		return convertEntityToDto(entity);
	}

	@Override
	public RoleDto getByRoleCode(String roleCode) {
		RoleEntity entity = roleRepository.findByRoleCode(roleCode);

		if (entity == null)
			throw new RuntimeException("Role not found");

		return convertEntityToDto(entity);
	}

	@Override
	public List<RoleDto> getAllRoles() {
		List<RoleEntity> entities = roleRepository.findAll();

		if (entities.isEmpty())
			throw new RuntimeException("No Roles Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public List<RoleDto> getAllRolesByAccount(String accountId) {
		List<RoleEntity> entities = roleRepository.findAllRolesByAccount(accountId);

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getRoles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<RoleEntity> entities = roleRepository.findAll(pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No Roles found under this account");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getRolesByAccount(String accountId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<RoleEntity> entities = roleRepository.findRolesByAccount(accountId, pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No Roles found under this account");

		return finalizePageResponse(entities);
	}

	@Override
	public List<RoleDto> getAllRolesByAccountAndStatus(String accountId, String status) {
		Status enumStatus = RoleEntity.Status.valueOf(status);
		List<RoleEntity> entities = roleRepository.findAllRolesByAccountAndStatus(accountId, enumStatus);

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getRolesByAccountAndStatus(String accountId, String status, int page, int size) {
		Status enumStatus = RoleEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);

		Page<RoleEntity> entities = roleRepository.findRolesByAccountAndStatus(accountId, enumStatus, pageable);

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return finalizePageResponse(entities);
	}

	@Override
	@Transactional
	public RoleDto save(RoleDto dto) {
		RoleEntity entity = null;

		if (dto.getId() != null) {
			entity = roleRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No Role found with ID: " + dto.getId());
		}

		RoleEntity savedEntity = roleRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	@Transactional
	public void delete(String id) {
		RoleEntity job = roleRepository.getOne(id);

		if (job == null)
			throw new RuntimeException("No Role found");

		roleRepository.delete(job);
	}

	@Override
	public void batchSave(List<RoleDto> dtos) {
		Set<RoleEntity> entities = new HashSet<RoleEntity>();

		for (RoleDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, roleRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		roleRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		roleRepository.deleteByIdIn(ids);
	}

	/*
	 * ============================== Service Util Methods
	 * =================================
	 */
	private RoleEntity convertDtoToEntity(RoleDto dto, RoleEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, RoleEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
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

		return entity;
	}

	private Map<String, Object> finalizePageResponse(Page<RoleEntity> response) {
		List<RoleEntity> entities = response.getContent();
		List<RoleDto> dtos = getDtosFromEntities(entities);

		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put("content", dtos);
		responseModel.put("currentPage", response.getNumber());
		responseModel.put("totalItems", response.getTotalElements());
		responseModel.put("totalPages", response.getTotalPages());
		return responseModel;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private RoleDto convertEntityToDto(RoleEntity entity) {
		RoleDto response = this.mapper.map(entity, RoleDto.class);
		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

	private List<RoleDto> getDtosFromEntities(List<RoleEntity> entities) {
		List<RoleDto> dtos = new ArrayList<RoleDto>();

		for (RoleEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
