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
import com.easybidding.app.ws.io.entity.AccountEntity.Status;
import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.AccountService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountyRepository countyRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public AccountServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<AccountDto, AccountEntity> entityMapping = new PropertyMap<AccountDto, AccountEntity>() {
		protected void configure() {
			skip().setJobs(null);
			skip().setCountry(null);
			skip().setState(null);
			skip().setCounty(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
			skip().setDateLastActive(null);
		}
	};

	PropertyMap<AccountEntity, AccountDto> dtoMapping = new PropertyMap<AccountEntity, AccountDto>() {
		protected void configure() {
			skip().setJobs(null);
		}
	};

	@Override
	public AccountDto getAccountById(String accountId) {
		AccountEntity entity = accountRepository.getOne(accountId);

		if (entity == null)
			throw new RuntimeException("Account not found");

		return convertEntityToDto(entity);
	}

	@Override
	public AccountDto getAccountByEmail(String email) {
		AccountEntity entity = accountRepository.findByEmail(email);

		if (entity == null)
			throw new RuntimeException("Account not found");

		return convertEntityToDto(entity);
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		List<AccountEntity> entities = accountRepository.findAll();

		if (entities.isEmpty())
			throw new RuntimeException("No Accounts Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public List<AccountDto> getAllAccountsByStatus(Status status) {
		List<AccountEntity> entities = accountRepository.findByStatus(status);

		if (entities.isEmpty())
			throw new RuntimeException("No Accounts Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public List<AccountDto> getAllAccountsByCounty(CountyEntity county) {
		List<AccountEntity> entities = accountRepository.findByCounty(county);

		if (entities.isEmpty())
			throw new RuntimeException("No Accounts Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public List<AccountDto> getAllAccountsByState(StateEntity state) {
		List<AccountEntity> entities = accountRepository.findByState(state);

		if (entities.isEmpty())
			throw new RuntimeException("No Accounts Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public List<AccountDto> getAllAccountsByCountry(CountryEntity country) {
		List<AccountEntity> entities = accountRepository.findByCountry(country);

		if (entities.isEmpty())
			throw new RuntimeException("No Accounts Found");

		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getAllAccounts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountEntity> entities = accountRepository.findAll(pageable);

		if (entities.getContent().isEmpty())
			throw new RuntimeException("No Accounts Found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getAccountsByStatus(Status status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountEntity> entities = accountRepository.findByStatus(status, pageable);

		if (entities.getContent().isEmpty())
			throw new RuntimeException("No Accounts Found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getAccountsByCounty(CountyEntity county, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountEntity> entities = accountRepository.findByCounty(county, pageable);

		if (entities.getContent().isEmpty())
			throw new RuntimeException("No Accounts Found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getAccountsByState(StateEntity state, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountEntity> entities = accountRepository.findByState(state, pageable);

		if (entities.getContent().isEmpty())
			throw new RuntimeException("No Accounts Found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getAccountsByCountry(CountryEntity country, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountEntity> entities = accountRepository.findByCountry(country, pageable);

		if (entities.getContent().isEmpty())
			throw new RuntimeException("No Accounts Found");

		return finalizePageResponse(entities);
	}

	@Override
	public AccountDto save(AccountDto dto) {
		AccountEntity entity = null;

		if (dto.getId() != null) {
			entity = accountRepository.getOne(dto.getId());
		
			if (entity == null)
				throw new RuntimeException("No Account found with ID: " + dto.getId());
		}

		AccountEntity savedEntity = accountRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	public void delete(String id) {
		AccountEntity entity = accountRepository.getOne(id);

		if (entity == null) {
			throw new RuntimeException("No Account Found");
		}
		accountRepository.delete(entity);
	}

	@Override
	public void batchSave(List<AccountDto> dtos) {
		Set<AccountEntity> entities = new HashSet<AccountEntity>();

		for (AccountDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, accountRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		accountRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		accountRepository.deleteByIdIn(ids);
	}

	/*
	 * ============================== Service Util Methods =================================
	 */
	private AccountEntity convertDtoToEntity(AccountDto dto, AccountEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, AccountEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		if (dto.getCountry() != null) {
			entity.setCountry(countryRepository.findByCountryCode(dto.getCountry().getCountryCode()));
		}

		if (dto.getState() != null && dto.getCountry() != null) {
			entity.setState(
					stateRepository.findByStateCode(dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		if (dto.getCounty() != null && dto.getState() != null && dto.getCountry() != null) {
			entity.setCounty(countyRepository.findByCountyCode(dto.getCounty().getCountyCode(),
					dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		return entity;
	}

	private Map<String, Object> finalizePageResponse(Page<AccountEntity> response) {
		List<AccountEntity> entities = response.getContent();
		List<AccountDto> dtos = getDtosFromEntities(entities);

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
	private AccountDto convertEntityToDto(AccountEntity entity) {
		AccountDto response = this.mapper.map(entity, AccountDto.class);
		return response;
	}

	private List<AccountDto> getDtosFromEntities(List<AccountEntity> entities) {
		List<AccountDto> dtos = new ArrayList<AccountDto>();

		for (AccountEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
