package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
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

import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.JobFileService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.JobFileDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/v1/files")
public class JobFileController {

	@Autowired
	JobFileService fileService;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobFileRepository fileRepository;

	@Autowired
	Utils utils;

	@GetMapping("/file/{id}")
	public JobFileDto getFile(@PathVariable String id) {
		return fileService.getFileById(id);
	}

	@GetMapping("/job/{jobId}")
	public List<JobFileDto> getAllFilesByJob(@PathVariable String jobId) {
		return fileService.getAllFilesByJob(jobId);
	}

	@GetMapping("/job/{jobId}/status/{status}")
	public List<JobFileDto> getAllFilesByJobAndStatus(@PathVariable String jobId, @PathVariable String status) {
		return fileService.getAllFilesByJobAndStatus(jobId, status);
	}

	@GetMapping("/job/{jobId}/page/{page}/size/{size}")
	public Map<String, Object> getFilesByJob(@PathVariable String jobId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return fileService.getFilesByJob(jobId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/job/{jobId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getFilesByJobAndStatus(@PathVariable String jobId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return fileService.getFilesByJobAndStatus(jobId, status, page, size);
	}

	@PostMapping("/file")
	public JobFileDto addFile(@Valid @RequestBody JobFileDto request) throws ParseException {
		return fileService.save(request);
	}

	@PutMapping("/file")
	public JobFileDto updateFile(@Valid @RequestBody JobFileDto request) throws ParseException {
		return fileService.save(request);
	}

	@DeleteMapping("/file/{id}")
	public OperationStatusModel deleteFile(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		fileService.deleteFile(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchSave(@Valid @RequestBody List<JobFileDto> request) throws ParseException {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		fileService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<JobFileDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (JobFileDto req : request) {
			ids.add(req.getId());
		}
		fileService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
