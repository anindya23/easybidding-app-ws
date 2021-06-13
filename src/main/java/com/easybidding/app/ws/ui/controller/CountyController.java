package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
import java.util.List;

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

import com.easybidding.app.ws.exception.ResourceNotFoundException;
import com.easybidding.app.ws.service.CountyService;
import com.easybidding.app.ws.shared.dto.CountyDto;

import io.swagger.annotations.ApiParam;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/counties")
public class CountyController {

	@Autowired
	CountyService countyService;

	@GetMapping("/countyCode/{countyCode}/stateCode/{stateCode}/countryCode/{countryCode}")
	public CountyDto getCounty(@PathVariable String countyCode, @PathVariable String stateCode,
			@PathVariable String countryCode) {
		return countyService.getCounty(countyCode, stateCode, countryCode);
	}

	@GetMapping("/stateCode/{stateCode}/countryCode/{countryCode}")
	public List<CountyDto> getCountiesByStateAndCountry(@PathVariable String stateCode,
			@PathVariable String countryCode) {
		return countyService.getCountiesByStateAndCountry(stateCode, countryCode);
	}

	@PostMapping("/county")
	public CountyDto createCounty(@Valid @RequestBody CountyDto request) throws ParseException {
		return countyService.save(request);
	}

	@PutMapping("/county")
	public CountyDto updateCounty(@Valid @RequestBody CountyDto request) throws ResourceNotFoundException {
		return countyService.save(request);
	}

	@DeleteMapping("/county/{id}")
	public String deleteCounty(
			@ApiParam(value = "County Id from which county object will delete from database table", required = true) @PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		return "delete county was called";
	}

}
