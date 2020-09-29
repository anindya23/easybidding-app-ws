package com.easybidding.app.ws.service;

import com.easybidding.app.ws.shared.dto.SystemInitParamDto;

public interface SystemInitParamService {

	SystemInitParamDto getByParamKey(String paramKey);

	SystemInitParamDto createParam(SystemInitParamDto paramDto);
}
