package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import com.easybidding.app.ws.shared.dto.JobFileDto;
import com.easybidding.app.ws.shared.dto.JobFilesDto;

public interface JobFileService {

	JobFileDto getFileById(String id);

	List<JobFileDto> getAllFiles();

	List<JobFileDto> getAllFilesByJob(String jobId);

	List<JobFileDto> getAllFilesByJobAndStatus(String jobId, String status);

	Map<String, Object> getFiles(int page, int size);

	Map<String, Object> getFilesByJob(String jobId, int page, int size);

	Map<String, Object> getFilesByJobAndStatus(String jobId, String status, int page, int size);

	JobFileDto save(JobFileDto dto);

	void deleteFile(String id);

	void batchSave(List<JobFileDto> dtos);

	void batchDelete(List<String> ids);

	void uploadFiles(JobFilesDto dto);

	byte[] getJobFile(String fileId);

	byte[] getAllFiles(String jobId, String accountId);

//	Resource load(String filename);
//
//	void deleteAll();
//
//	Stream<Path> loadAll();

}
