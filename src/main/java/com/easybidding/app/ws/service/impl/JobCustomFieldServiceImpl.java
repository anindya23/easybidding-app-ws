package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.io.entity.JobCustomFieldEntity;
import com.easybidding.app.ws.io.entity.JobCustomFieldEntity.Status;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.JobCustomFieldRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.JobCustomFieldService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.shared.dto.JobCustomFieldDto;
import com.easybidding.app.ws.shared.dto.JobDto;

@Service
public class JobCustomFieldServiceImpl implements JobCustomFieldService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobCustomFieldRepository fieldRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public JobCustomFieldServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<JobCustomFieldDto, JobCustomFieldEntity> entityMapping = new PropertyMap<JobCustomFieldDto, JobCustomFieldEntity>() {
		protected void configure() {
			skip().setJob(null);
		}
	};

	PropertyMap<JobCustomFieldEntity, JobCustomFieldDto> dtoMapping = new PropertyMap<JobCustomFieldEntity, JobCustomFieldDto>() {
		protected void configure() {
			skip().setJob(null);
			skip().setAccount(null);
		}
	};

	@Override
	public JobCustomFieldDto getFieldById(String id) {
		JobCustomFieldEntity entity = fieldRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("No Field found");

		return convertEntityToDto(entity);
	}

	@Override
	public List<JobCustomFieldDto> getAllFieldsByJob(String jobId) {
		List<JobCustomFieldEntity> entities = fieldRepository.findAllFieldsByJob(jobId);

		if (entities == null) {
			throw new RuntimeException("No Fields found under this Job");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<JobCustomFieldDto> getAllFieldsByJobAndStatus(String jobId, String status) {
		Status enumStatus = JobCustomFieldEntity.Status.valueOf(status);
		List<JobCustomFieldEntity> entities = fieldRepository.findAllFieldsByJobAndStatus(jobId, enumStatus);

		if (entities == null) {
			throw new RuntimeException("No Fields found under this Job");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getFieldsByJob(String jobId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<JobCustomFieldEntity> fields = fieldRepository.findFieldsByJob(jobId, pageable);

		if (fields.getContent() == null)
			throw new RuntimeException("No Fields found under this Job");

		return finalizePageResponse(fields);
	}

	@Override
	public Map<String, Object> getFieldsByJobAndStatus(String jobId, String status, int page, int size) {
		Status enumStatus = JobCustomFieldEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);

		Page<JobCustomFieldEntity> fields = fieldRepository.findFieldsByJobAndStatus(jobId, enumStatus, pageable);

		if (fields.getContent() == null)
			throw new RuntimeException("No Fields found under this Job");

		return finalizePageResponse(fields);
	}

	/*
	 * Refactoring needed 1. Fetch Jobs one request 2. Batch Processing 3.
	 * Transactional 4. Job exists validation
	 */
	@Override
	@Transactional
	public JobCustomFieldDto save(JobCustomFieldDto dto) {
		JobCustomFieldEntity entity = null;

		if (dto.getId() != null) {
			entity = fieldRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No Field found with ID: " + dto.getId());
		}

		JobCustomFieldEntity savedEntity = fieldRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	@Transactional
	public void deleteCustomField(String id) {
		JobCustomFieldEntity job = fieldRepository.getOne(id);

		if (job == null)
			throw new RuntimeException("No Fields found");

		fieldRepository.delete(job);
	}

	@Override
	public void batchSave(List<JobCustomFieldDto> dtos) {
		Set<JobCustomFieldEntity> entities = new HashSet<JobCustomFieldEntity>();

		for (JobCustomFieldDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, fieldRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		fieldRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		fieldRepository.deleteByIdIn(ids);
	}

	/*
	 * ============================== Service Util Methods
	 * =================================
	 */
	private JobCustomFieldEntity convertDtoToEntity(JobCustomFieldDto dto, JobCustomFieldEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, JobCustomFieldEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		if (dto.getJob() != null) {
			entity.setJob(jobRepository.getOne(dto.getJob().getId()));
		}
		return entity;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private JobCustomFieldDto convertEntityToDto(JobCustomFieldEntity entity) {
		JobCustomFieldDto response = this.mapper.map(entity, JobCustomFieldDto.class);

		if (entity.getJob() != null) {
			JobDto jobDto = new JobDto();
			jobDto.setId(entity.getJob().getId());
			jobDto.setJobTitle(entity.getJob().getJobTitle());
			response.setJob(jobDto);
		}

		if (entity.getAccount() != null) {
			AccountDto accountDto = new AccountDto();
			accountDto.setId(entity.getAccount().getId());
			accountDto.setAccountName(entity.getAccount().getAccountName());
			response.setAccount(accountDto);
		}
		return response;
	}

	private Map<String, Object> finalizePageResponse(Page<JobCustomFieldEntity> response) {
		List<JobCustomFieldEntity> entities = response.getContent();
		List<JobCustomFieldDto> dtos = getDtosFromEntities(entities);

		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put("content", dtos);
		responseModel.put("currentPage", response.getNumber());
		responseModel.put("totalItems", response.getTotalElements());
		responseModel.put("totalPages", response.getTotalPages());
		return responseModel;
	}

	private List<JobCustomFieldDto> getDtosFromEntities(List<JobCustomFieldEntity> entities) {
		List<JobCustomFieldDto> dtos = new ArrayList<JobCustomFieldDto>();

		for (JobCustomFieldEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
