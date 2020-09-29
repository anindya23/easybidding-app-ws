package com.easybidding.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.io.entity.AccountEntity.Status;
import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.AccountService;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/v1/admin/accounts")
@PreAuthorize("hasAuthority('SYS_ADMIN')")
public class AccountsController {

	@Autowired
	AccountService accountService;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountyRepository countyRepository;

	@GetMapping("/account/{id}")
	public AccountDto getAccount(@PathVariable String id) {
		return accountService.getAccountById(id);
	}

	@GetMapping("/account/email/{email}")
	public AccountDto getAccountByEmail(@PathVariable String email) {
		return accountService.getAccountByEmail(email);
	}

	@GetMapping
	public List<AccountDto> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	@GetMapping("/status/{status}")
	public List<AccountDto> getAllAccountsByStatus(@PathVariable String status) {
		return accountService.getAllAccountsByStatus(Status.valueOf(status));
	}

	@GetMapping("/country/{countryCode}")
	public List<AccountDto> getAllAccountsByCountry(@PathVariable String countryCode) {
		CountryEntity country = countryRepository.findByCountryCode(countryCode);

		if (country == null)
			throw new RuntimeException("No Country Found");

		return accountService.getAllAccountsByCountry(country);
	}

	@GetMapping("/state/{stateCode}/country/{countryCode}")
	public List<AccountDto> getAllAccountsByState(@PathVariable String stateCode, @PathVariable String countryCode) {
		StateEntity state = stateRepository.findByStateCode(stateCode, countryCode);

		if (state == null)
			throw new RuntimeException("No State Found");

		return accountService.getAllAccountsByState(state);
	}

	@GetMapping("/county/{countyCode}/state/{stateCode}/country/{countryCode}")
	public List<AccountDto> getAllAccountsByCounty(@PathVariable String countyCode, @PathVariable String stateCode,
			@PathVariable String countryCode) {
		CountyEntity county = countyRepository.findByCountyCode(countyCode, stateCode, countryCode);

		if (county == null)
			throw new RuntimeException("No County Found");

		return accountService.getAllAccountsByCounty(county);
	}

	@GetMapping("/page/{page}/size/{size}")
	public Map<String, Object> getAllAccounts(@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return accountService.getAllAccounts(page, size);
	}

	@GetMapping("/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getAllAccounts(@PathVariable String status, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return accountService.getAccountsByStatus(Status.valueOf(status), page, size);
	}

	@GetMapping("/county/{countyCode}/state/{stateCode}/country/{countryCode}/page/{page}/size/{size}")
	public Map<String, Object> getAccountsByCounty(@PathVariable String countyCode, @PathVariable String stateCode,
			@PathVariable String countryCode, @PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		CountyEntity county = countyRepository.findByCountyCode(countyCode, stateCode, countryCode);

		if (county == null)
			throw new RuntimeException("No County Found");

		return accountService.getAccountsByCounty(county, page, size);
	}

	@GetMapping("/state/{stateCode}/country/{countryCode}/page/{page}/size/{size}")
	public Map<String, Object> getAccountsByState(@PathVariable String stateCode, @PathVariable String countryCode,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		StateEntity state = stateRepository.findByStateCode(stateCode, countryCode);

		if (state == null)
			throw new RuntimeException("No State Found");

		return accountService.getAccountsByState(state, page, size);
	}

	@GetMapping("/country/{countryCode}/page/{page}/size/{size}")
	public Map<String, Object> getAccountsByCountry(@PathVariable String countryCode, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		CountryEntity country = countryRepository.findByCountryCode(countryCode);

		if (country == null)
			throw new RuntimeException("No Country Found");

		return accountService.getAccountsByCountry(country, page, size);
	}

	@PostMapping("/account")
	public AccountDto createAccount(@Valid @RequestBody AccountDto request) {
		return accountService.save(request);
	}

	@PutMapping("/account")
	public AccountDto updateAccount(@Valid @RequestBody AccountDto request) {
		return accountService.save(request);
	}

	@DeleteMapping("/account/{id}")
	public OperationStatusModel deleteAccount(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		accountService.delete(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchInsert(@Valid @RequestBody List<AccountDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		accountService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PutMapping
	public OperationStatusModel batchUpdate(@Valid @RequestBody List<AccountDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHUPDATE.name());

		accountService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<AccountDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (AccountDto req : request) {
			ids.add(req.getId());
		}

		accountService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
