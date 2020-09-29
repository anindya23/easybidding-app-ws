package com.easybidding.app.ws.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybidding.app.ws.io.entity.SystemInitParamEntity;
import com.easybidding.app.ws.repository.impl.SystemInitParamRepository;
import com.easybidding.app.ws.service.SystemInitParamService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.SystemInitParamDto;

@Service
public class SystemInitParamServiceImpl implements SystemInitParamService {

	@Autowired
	SystemInitParamRepository paramRepository;

	@Autowired
	Utils utils;

	@Autowired
	ModelMapper mapper;

	@Override
	public SystemInitParamDto getByParamKey(String paramKey) {
		SystemInitParamEntity entity = paramRepository.findByParamKey(paramKey);

		if (entity == null)
			throw new RuntimeException("Parameter doesn't exist");

		return mapper.map(entity, SystemInitParamDto.class);
	}

	@Override
	public SystemInitParamDto createParam(SystemInitParamDto paramDto) {
		SystemInitParamEntity entity = new SystemInitParamEntity();
		BeanUtils.copyProperties(paramDto, entity);

		entity.setId(utils.generateUniqueId(30));
		entity.setDateCreated(new Date());
		entity.setDateLastUpdated(new Date());

		SystemInitParamEntity savedParam = paramRepository.save(entity);
		return mapper.map(savedParam, SystemInitParamDto.class);
	}

}
