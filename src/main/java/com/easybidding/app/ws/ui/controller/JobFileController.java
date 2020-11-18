package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.easybidding.app.ws.ui.model.response.ResponseMessage;

@CrossOrigin("*")
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

//	@PostMapping
//	public JobFileDto addFile(@Valid @RequestBody JobFileDto request) throws ParseException {
//		return jobFileService.save(request);
//	}
	
	@PostMapping
	public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
		String message = "";
		try {
			List<String> fileNames = new ArrayList<>();
			Arrays.asList(files).stream().forEach(file -> {
				filesService.upload(file);
				fileNames.add(file.getOriginalFilename());
			});
			message = "Uploaded the files successfully: " + fileNames;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Fail to upload files!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadJobFiles(@ModelAttribute JobFilesDto dto) {
		String message = "";
		try {
			jobService.uploadFiles(dto);
			message = "Uploaded the files successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Fail to upload files!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

//	@GetMapping("/files")
//	public ResponseEntity<List<FileInfo>> getListFiles() {
//		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}

//	@GetMapping("/files/{filename:.+}")
//	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//		Resource file = storageService.load(filename);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

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

//	@PostMapping
//	public OperationStatusModel batchSave(@Valid @RequestBody List<JobFileDto> request) throws ParseException {
//		OperationStatusModel response = new OperationStatusModel();
//		response.setOperationName(RequestOperationName.BATCHINSERT.name());
//
//		jobFileService.batchSave(request);
//
//		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
//		return response;
//	}
//
//	@DeleteMapping
//	public OperationStatusModel batchDelete(@Valid @RequestBody List<JobFileDto> request) {
//		OperationStatusModel response = new OperationStatusModel();
//		response.setOperationName(RequestOperationName.BATCHDELETE.name());
//
//		List<String> ids = new ArrayList<String>();
//
//		for (JobFileDto req : request) {
//			ids.add(req.getId());
//		}
//		jobFileService.batchDelete(ids);
//
//		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
//		return response;
//	}

}
