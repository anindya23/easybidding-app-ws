package com.easybidding.app.ws.service;

import com.easybidding.app.ws.shared.dto.AccountInitParamDto;

public interface AccountInitParamService {

	AccountInitParamDto getParamsByAccount(String accountId);

	AccountInitParamDto getByParamKey(String paramKey);

	AccountInitParamDto createParam(AccountInitParamDto paramDto);
}
