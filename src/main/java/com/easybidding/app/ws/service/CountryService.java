package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import com.easybidding.app.ws.shared.dto.CountryDto;

public interface CountryService {

	CountryDto getCountryById(String id);

	CountryDto getCountryByCode(String countryCode);

	List<CountryDto> getAllCountries();

	List<CountryDto> getCountriesByStatus(String status);

	Map<String, Object> getAllCountries(int page, int size);

	Map<String, Object> getCountriesByStatus(String status, int page, int size);

	CountryDto save(CountryDto dto);

	void delete(String id);

	void batchSave(List<CountryDto> dtos);

	void batchDelete(List<String> ids);

}
