package com.easybidding.app.ws.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;

import com.easybidding.app.ws.shared.dto.JobDto;
import com.easybidding.app.ws.shared.dto.JobFilesDto;

public interface JobService {

	JobDto getJobById(String jobId);

	List<JobDto> getAllJobs();

	List<JobDto> getAllJobsByAccount(String accountId);

	List<JobDto> getAllJobsByAccountAndStatus(String accountId, String status);

	Map<String, Object> getJobs(int page, int size);

	Map<String, Object> getJobsByAccount(String accountId, int page, int size);

	Map<String, Object> getJobsByAccountAndStatus(String accountId, String status, int page, int size);

	JobDto save(JobDto jobDto);
	
	void deleteJob(String id);

	void batchSave(List<JobDto> dtos);

	void batchDelete(List<String> ids);
	
	void uploadFiles(JobFilesDto dto);

	Resource load(String filename);

	void deleteAll();

	Stream<Path> loadAll();


}
