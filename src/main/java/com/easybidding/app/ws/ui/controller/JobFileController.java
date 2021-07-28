package com.easybidding.app.ws.ui.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.FilesStorageService;
import com.easybidding.app.ws.service.JobFileService;
import com.easybidding.app.ws.service.JobService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.JobFileDto;
import com.easybidding.app.ws.shared.dto.JobFilesDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/files")
public class JobFileController {

	@Autowired
	JobFileService jobFileService;

	@Autowired
	JobService jobService;

	@Autowired
	FilesStorageService filesService;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobFileRepository fileRepository;

	@Autowired
	Utils utils;

	@GetMapping("/file/{id}")
	public JobFileDto getFile(@PathVariable String id) {
		return jobFileService.getFileById(id);
	}

	@GetMapping("/job/{jobId}")
	public List<JobFileDto> getAllFilesByJob(@PathVariable String jobId) {
		return jobFileService.getAllFilesByJob(jobId);
	}

	@GetMapping("/job/{jobId}/status/{status}")
	public List<JobFileDto> getAllFilesByJobAndStatus(@PathVariable String jobId, @PathVariable String status) {
		return jobFileService.getAllFilesByJobAndStatus(jobId, status);
	}

	@GetMapping("/job/{jobId}/page/{page}/size/{size}")
	public Map<String, Object> getFilesByJob(@PathVariable String jobId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobFileService.getFilesByJob(jobId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/job/{jobId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getFilesByJobAndStatus(@PathVariable String jobId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return jobFileService.getFilesByJobAndStatus(jobId, status, page, size);
	}

	@PostMapping("/upload")
	public List<JobFileDto> uploadJobFiles(@ModelAttribute JobFilesDto dto) {
		return jobFileService.uploadFiles(dto);
	}

	@GetMapping(path = "/download/file/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") String fileId) throws IOException {
		JobFileDto dto = jobFileService.getFileById(fileId);

		byte[] data = jobFileService.getJobFile(fileId);
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + dto.getFileName() + "\"").body(resource);
	}

	@GetMapping(path = "/download/job/{jobId}/account/{accountId}", produces = "application/zip")
	public ResponseEntity<Resource> downloadFilesByAccount(@PathVariable("jobId") String jobId, @PathVariable("accountId") String accountId,
			HttpServletResponse response) throws IOException {
		return jobFileService.getAllFiles(response, jobId, accountId);
	}
	
	@GetMapping(path = "/download/job/{jobId}", produces = "application/zip")
	public ResponseEntity<Resource> downloadFilesByJob(@PathVariable("jobId") String jobId,
			HttpServletResponse response) throws IOException {
		return jobFileService.getAllFiles(response, jobId, null);
	}

	@PutMapping("/file")
	public JobFileDto updateFile(@Valid @RequestBody JobFileDto request) throws ParseException {
		return jobFileService.save(request);
	}

	@DeleteMapping("/file/{id}")
	public OperationStatusModel deleteFile(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		jobFileService.deleteFile(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
