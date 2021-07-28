package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.repository.impl.UserRepository;
import com.easybidding.app.ws.service.JobService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.JobDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

//@PreAuthorize("hasRole('SYS_ADMIN')")
//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/jobs")
public class JobController {

	@Autowired
	JobService jobService;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountyRepository countyRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ModelMapper mapper;

	@Autowired
	Utils utils;

	@GetMapping("/job/{id}")
	public JobDto getJob(@PathVariable String id) {
		return jobService.getJobById(id);
	}

	@GetMapping("/account/{accountId}")
	public List<JobDto> getAllJobsByAccount(@PathVariable String accountId) {
		return jobService.getAllJobsByAccount(accountId);
	}

	@GetMapping("/account/{accountId}/status/{status}")
	public List<JobDto> getAllJobsByAccountAndStatus(@PathVariable String accountId, @PathVariable String status) {
		return jobService.getAllJobsByAccountAndStatus(accountId, status);
	}

	@GetMapping("/user/{email}/status/{status}")
	public List<JobDto> getAllJobsByUserAndStatus(@PathVariable String email, @PathVariable String status) {
		UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return jobService.getAllJobsByAccountAndStatus(user.getAccount().getId(), status);
	}

	@GetMapping("/account/{accountId}/page/{page}/size/{size}")
	public Map<String, Object> getJobsByAccount(@PathVariable String accountId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobService.getJobsByAccount(accountId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/account/{accountId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getJobsByAccountAndStatus(@PathVariable String accountId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobService.getJobsByAccountAndStatus(accountId, status, page, size);
	}

	@PostMapping("/job")
	public JobDto createJob(@Valid @RequestBody JobDto request) throws ParseException {
		return jobService.save(request);
	}

	@PutMapping("/job")
	public JobDto updateJob(@Valid @RequestBody JobDto request) throws ParseException {
		return jobService.save(request);
	}

	@DeleteMapping("/job/{id}")
	public OperationStatusModel deleteJob(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		jobService.deleteJob(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchInsert(@Valid @RequestBody List<JobDto> request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		jobService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PutMapping
	public OperationStatusModel batchUpdate(@Valid @RequestBody List<JobDto> request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		jobService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<JobDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (JobDto req : request) {
			ids.add(req.getId());
		}

		jobService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
