package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.StateService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.StateDto;

@Service
public class StateServiceImpl implements StateService {

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public StateServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<StateDto, StateEntity> entityMapping = new PropertyMap<StateDto, StateEntity>() {
		protected void configure() {
			skip().setCountry(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<StateEntity, StateDto> dtoMapping = new PropertyMap<StateEntity, StateDto>() {
		protected void configure() {
			skip().setCounties(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	@Override
	public List<StateDto> getStatesByCountry(String stateCode) {
		List<StateEntity> entities = stateRepository.findByCountry(stateCode);

		if (entities == null)
			throw new RuntimeException("State doesn't exist");

		return getDtosFromEntities(entities);
	}

	@Override
	public StateDto getState(String stateCode, String countryCode) {
		StateEntity entity = stateRepository.findByStateCode(stateCode, countryCode);

		if (entity == null)
			throw new RuntimeException("State doesn't exist");

		return convertEntityToDto(entity);
	}

	@Override
	public StateDto save(StateDto dto) {
		StateEntity entity = null;

		if (dto.getId() != null) {
			entity = stateRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No State found with ID: " + dto.getId());
		}

		StateEntity savedEntity = stateRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	/*
	 * ============================== Service Util Methods =================================
	 */
	private StateEntity convertDtoToEntity(StateDto dto, StateEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, StateEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		if (dto.getCountry().getCountryCode() != null) {
			entity.setCountry(countryRepository.findByCountryCode(dto.getCountry().getCountryCode()));
		}

		return entity;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private StateDto convertEntityToDto(StateEntity entity) {
		StateDto response = this.mapper.map(entity, StateDto.class);
		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

	private List<StateDto> getDtosFromEntities(List<StateEntity> entities) {
		List<StateDto> dtos = new ArrayList<StateDto>();

		for (StateEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
