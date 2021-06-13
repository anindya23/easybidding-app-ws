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
import com.easybidding.app.ws.service.StateService;
import com.easybidding.app.ws.shared.dto.StateDto;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/states")
public class StateController {

	@Autowired
	StateService stateService;

	@GetMapping("/stateCode/{stateCode}/countryCode/{countryCode}")
	public StateDto getState(@PathVariable String stateCode, @PathVariable String countryCode) {
		return stateService.getState(stateCode, countryCode);
	}

	@GetMapping("/countryCode/{countryCode}")
	public List<StateDto> getStatesByCountry(@PathVariable String countryCode) {
		return stateService.getStatesByCountry(countryCode);
	}

	@PostMapping
	public StateDto save(@Valid @RequestBody StateDto request) throws ParseException {
		return stateService.save(request);
	}

	@PutMapping
	public StateDto updateCounty(@Valid @RequestBody StateDto request) throws ResourceNotFoundException {
		return stateService.save(request);
	}

	@DeleteMapping("/{id}")
	public String deleteState(@PathVariable(value = "id") String id) throws ResourceNotFoundException {
		return "delete state was called";
	}

}
