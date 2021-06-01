package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import com.easybidding.app.ws.shared.dto.JobCustomFieldDto;

public interface JobCustomFieldService {

	JobCustomFieldDto getFieldById(String id);

	List<JobCustomFieldDto> getAllFieldsByJob(String jobId);

	List<JobCustomFieldDto> getAllFieldsByJobAndStatus(String jobId, String status);

	Map<String, Object> getFieldsByJob(String jobId, int page, int size);

	Map<String, Object> getFieldsByJobAndStatus(String jobId, String status, int page, int size);

	JobCustomFieldDto save(JobCustomFieldDto dto);

	void deleteCustomField(String id);

	void batchSave(List<JobCustomFieldDto> dtos);

	void batchDelete(List<String> ids);

}
