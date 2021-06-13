package com.easybidding.app.ws.ui.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.repository.impl.JobCustomFieldRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.JobCustomFieldService;
import com.easybidding.app.ws.service.JobService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.JobCustomFieldDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/custom/fields")
public class JobCustomFieldController {

	@Autowired
	JobCustomFieldService jobFieldService;

	@Autowired
	JobService jobService;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobCustomFieldRepository fieldRepository;

	@Autowired
	Utils utils;

	@GetMapping("/field/{id}")
	public JobCustomFieldDto getFile(@PathVariable String id) {
		return jobFieldService.getFieldById(id);
	}

	@GetMapping("/job/{jobId}")
	public List<JobCustomFieldDto> getAllFilesByJob(@PathVariable String jobId) {
		return jobFieldService.getAllFieldsByJob(jobId);
	}

	@GetMapping("/job/{jobId}/status/{status}")
	public List<JobCustomFieldDto> getAllFilesByJobAndStatus(@PathVariable String jobId, @PathVariable String status) {
		return jobFieldService.getAllFieldsByJobAndStatus(jobId, status);
	}

	@GetMapping("/job/{jobId}/page/{page}/size/{size}")
	public Map<String, Object> getFieldsByJob(@PathVariable String jobId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobFieldService.getFieldsByJob(jobId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/job/{jobId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getFieldsByJobAndStatus(@PathVariable String jobId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobFieldService.getFieldsByJobAndStatus(jobId, status, page, size);
	}

	@PostMapping("/field")
	public JobCustomFieldDto createField(@ModelAttribute JobCustomFieldDto dto) {
		return jobFieldService.save(dto);
	}

	@PutMapping("/field")
	public JobCustomFieldDto updateField(@ModelAttribute JobCustomFieldDto dto) {
		return jobFieldService.save(dto);
	}

	@DeleteMapping("/field/{id}")
	public OperationStatusModel deleteField(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		jobFieldService.deleteCustomField(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
