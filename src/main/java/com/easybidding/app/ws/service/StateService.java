package com.easybidding.app.ws.service;

import java.util.List;

import com.easybidding.app.ws.shared.dto.StateDto;

public interface StateService {

	List<StateDto> getStatesByCountry(String countryCode);

	StateDto getState(String stateCode, String countryCode);

	StateDto save(StateDto dto);

	void batchSave(List<StateDto> dtos);
}
