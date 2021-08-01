package com.easybidding.app.ws.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountryEntity.Status;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.service.CountryService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.CountryDto;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public CountryServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<CountryDto, CountryEntity> entityMapping = new PropertyMap<CountryDto, CountryEntity>() {
		protected void configure() {
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<CountryEntity, CountryDto> dtoMapping = new PropertyMap<CountryEntity, CountryDto>() {
		protected void configure() {
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	@Override
	public CountryDto getCountryById(String id) {
		CountryEntity entity = countryRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("Country doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public CountryDto getCountryByCode(String countryCode) {
		CountryEntity entity = countryRepository.findByCountryCode(countryCode);

		if (entity == null)
			throw new RuntimeException("Country doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public List<CountryDto> getCountriesByStatus(String status) {
		Status enumStatus = CountryEntity.Status.valueOf(status);
		List<CountryEntity> entities = countryRepository.findByStatus(enumStatus);
		return entities.stream().map(entity -> this.mapper.map(entity, CountryDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<CountryDto> getAllCountries() {
		List<CountryEntity> entities = countryRepository.findAll();

		if (entities == null)
			throw new RuntimeException("No Accounts Found");

		return entities.stream().map(entity -> this.mapper.map(entity, CountryDto.class)).collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getAllCountries(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CountryEntity> entities = countryRepository.findAll(pageable);

		if (entities.getContent() == null)
			throw new RuntimeException("No Country Found");

		return finalizePageResponse(entities);
	}

	@Override
	public Map<String, Object> getCountriesByStatus(String status, int page, int size) {
		Status enumStatus = CountryEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);
		Page<CountryEntity> entities = countryRepository.findByStatus(enumStatus, pageable);
		return finalizePageResponse(entities);
	}

	@Override
	public CountryDto save(CountryDto dto) {
		CountryEntity entity = null;

		if (dto.getId() != null) {
			entity = countryRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No Country found with ID: " + dto.getId());
		}

		CountryEntity savedEntity = countryRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	public void delete(String id) {
		CountryEntity entity = countryRepository.getOne(id);

		if (entity == null) {
			throw new RuntimeException("No Country Found");
		}
		countryRepository.delete(entity);
	}

	@Override
	public void batchSave(List<CountryDto> dtos) {
		Set<CountryEntity> entities = new HashSet<CountryEntity>();

		for (CountryDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, countryRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		countryRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		countryRepository.deleteByIdIn(ids);
	}

	/*
	 * ============================== Service Util Methods =================================
	 */
	private CountryEntity convertDtoToEntity(CountryDto dto, CountryEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, CountryEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		return entity;
	}

	private Map<String, Object> finalizePageResponse(Page<CountryEntity> response) {
		List<CountryEntity> entities = response.getContent();
		List<CountryDto> dtos = entities.stream().map(entity -> this.mapper.map(entity, CountryDto.class))
				.collect(Collectors.toList());

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
	private CountryDto convertEntityToDto(CountryEntity entity) {
		CountryDto response = this.mapper.map(entity, CountryDto.class);
		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

}
