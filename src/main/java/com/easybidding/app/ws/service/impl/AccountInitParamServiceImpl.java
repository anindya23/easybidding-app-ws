package com.easybidding.app.ws.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybidding.app.ws.io.entity.AccountInitParamEntity;
import com.easybidding.app.ws.repository.impl.AccountInitParamRepository;
import com.easybidding.app.ws.service.AccountInitParamService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountInitParamDto;

@Service
public class AccountInitParamServiceImpl implements AccountInitParamService {

	@Autowired
	AccountInitParamRepository paramRepository;

	@Autowired
	ModelMapper mapper;

	@Autowired
	Utils utils;

	@Override
	public AccountInitParamDto getByParamKey(String paramKey) {
		AccountInitParamEntity entity = paramRepository.findByParamKey(paramKey);

		if (entity == null)
			throw new RuntimeException("Parameter doesn't exist");

		return mapper.map(entity, AccountInitParamDto.class);
	}

	@Override
	public AccountInitParamDto getParamsByAccount(String accountId) {
		AccountInitParamEntity entity = paramRepository.findByAccountId(accountId);

		if (entity == null)
			throw new RuntimeException("Parameter doesn't exist");

		return mapper.map(entity, AccountInitParamDto.class);
	}

	@Override
	public AccountInitParamDto createParam(AccountInitParamDto paramDto) {
		AccountInitParamEntity entity = mapper.map(paramDto, AccountInitParamEntity.class);
		entity.setId(utils.generateUniqueId(30));
		entity.setDateCreated(new Date());
		entity.setDateLastUpdated(new Date());

		AccountInitParamEntity savedParam = paramRepository.save(entity);
		return mapper.map(savedParam, AccountInitParamDto.class);
	}

}
