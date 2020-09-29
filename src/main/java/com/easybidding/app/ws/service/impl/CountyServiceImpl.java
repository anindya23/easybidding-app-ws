package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.CountyService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.CountyDto;

@Service
public class CountyServiceImpl implements CountyService {

	@Autowired
	CountyRepository countyRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public CountyServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<CountyDto, CountyEntity> entityMapping = new PropertyMap<CountyDto, CountyEntity>() {
		protected void configure() {
			skip().setCountry(null);
			skip().setState(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<CountyEntity, CountyDto> dtoMapping = new PropertyMap<CountyEntity, CountyDto>() {
		protected void configure() {
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
//			skip().setDateCreated(null, null);
//			skip().setDateLastUpdated(null, null);
		}
	};

	@Override
	public List<CountyDto> getCountiesByStateAndCountry(String stateCode, String countryCode) {
		List<CountyEntity> entities = countyRepository.findByStateAndCountry(stateCode, countryCode);

		if (entities == null)
			throw new RuntimeException("No Counties are under State and Country");

		return getDtosFromEntities(entities);
	}

	@Override
	public CountyDto getCounty(String countyCode, String stateCode, String countryCode) {
		CountyEntity entity = countyRepository.findByCountyCode(countyCode, stateCode, countryCode);

		if (entity == null)
			throw new RuntimeException("County doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public CountyDto save(CountyDto dto) {
		CountyEntity entity = null;

		if (dto.getId() != null) {
			entity = countyRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No County found with ID: " + dto.getId());
		}

		CountyEntity savedEntity = countyRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	/*
	 * ============================== Service Util Methods
	 * =================================
	 */
	private CountyEntity convertDtoToEntity(CountyDto dto, CountyEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, CountyEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		if (dto.getCountry().getCountryCode() != null) {
			entity.setCountry(countryRepository.findByCountryCode(dto.getCountry().getCountryCode()));
		}

		if (dto.getState().getStateCode() != null) {
			entity.setState(
					stateRepository.findByStateCode(dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		return entity;
	}

//	private Map<String, Object> finalizePageResponse(Page<CountyEntity> response) {
//		List<CountyEntity> entities = response.getContent();
//		List<CountyDto> dtos = getDtosFromEntities(entities);
//
//		Map<String, Object> responseModel = new HashMap<>();
//		responseModel.put("content", dtos);
//		responseModel.put("currentPage", response.getNumber());
//		responseModel.put("totalItems", response.getTotalElements());
//		responseModel.put("totalPages", response.getTotalPages());
//		return responseModel;
//	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private CountyDto convertEntityToDto(CountyEntity entity) {
		CountyDto response = this.mapper.map(entity, CountyDto.class);
		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

	private List<CountyDto> getDtosFromEntities(List<CountyEntity> entities) {
		List<CountyDto> dtos = new ArrayList<CountyDto>();

		for (CountyEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
