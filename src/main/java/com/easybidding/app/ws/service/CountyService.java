package com.easybidding.app.ws.service;

import java.util.List;

import com.easybidding.app.ws.shared.dto.CountyDto;

public interface CountyService {

	List<CountyDto> getCountiesByStateAndCountry(String stateCode, String countryCode);

	CountyDto getCounty(String countyCode, String stateCode, String countryCode);

	CountyDto save(CountyDto countyDto);
}
