package com.easybidding.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.service.CountryService;
import com.easybidding.app.ws.shared.dto.CountryDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

	@Autowired
	CountryService countryService;

	@GetMapping("/country/{id}")
	public CountryDto getCountry(@PathVariable String id) {
		return countryService.getCountryById(id);
	}

	@GetMapping("/country/code/{code}")
	public CountryDto getCountryByCode(@PathVariable String code) {
		return countryService.getCountryByCode(code);
	}

	@GetMapping
	public List<CountryDto> getAllCountries() {
		return countryService.getAllCountries();
	}

	@GetMapping("/status/{status}")
	public List<CountryDto> getCountriesByStatus(@PathVariable String status) {
		return countryService.getCountriesByStatus(status);
	}

	@GetMapping("/page/{page}/size/{size}")
	public Map<String, Object> getAllCountries(@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return countryService.getAllCountries(page, size);
	}

	@GetMapping("/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getCountriesByStatus(@PathVariable String status, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return countryService.getCountriesByStatus(status, page, size);
	}

	@PostMapping("/country")
	public CountryDto createCountry(@Valid @RequestBody CountryDto request) {
		return countryService.save(request);
	}

	@PutMapping("/country")
	public CountryDto updateCountry(@Valid @RequestBody CountryDto request) {
		return countryService.save(request);
	}

	@DeleteMapping("/country/{id}")
	public OperationStatusModel deleteCountry(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		countryService.delete(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchSave(@Valid @RequestBody List<CountryDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		countryService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<CountryDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();
		for (CountryDto req : request) {
			ids.add(req.getId());
		}
		countryService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
