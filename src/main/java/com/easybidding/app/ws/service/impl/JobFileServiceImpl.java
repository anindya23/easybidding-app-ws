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

import com.easybidding.app.ws.io.entity.JobFileEntity;
import com.easybidding.app.ws.io.entity.JobFileEntity.Status;
import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.JobFileService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.JobFileDto;

@Service
public class JobFileServiceImpl implements JobFileService {

//	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobFileRepository fileRepository;

	@Autowired
	Utils utils;

	private ModelMapper mapper;

	@Autowired
	public JobFileServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(entityMapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<JobFileDto, JobFileEntity> entityMapping = new PropertyMap<JobFileDto, JobFileEntity>() {
		protected void configure() {
			skip().setJob(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<JobFileEntity, JobFileDto> dtoMapping = new PropertyMap<JobFileEntity, JobFileDto>() {
		protected void configure() {
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
//			skip().setDateCreated(null, null);
//			skip().setDateLastUpdated(null, null);
		}
	};

	@Override
	public JobFileDto getFileById(String id) {
		JobFileEntity entity = fileRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("No Files found");

		return convertEntityToDto(entity);
	}

	@Override
	public List<JobFileDto> getAllFiles() {
		List<JobFileEntity> entities = fileRepository.findAll();

		if (entities == null) {
			throw new RuntimeException("No Files found under this Job");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<JobFileDto> getAllFilesByJob(String jobId) {
		List<JobFileEntity> entities = fileRepository.findAllFilesByJob(jobId);

		if (entities == null) {
			throw new RuntimeException("No Files found under this Job");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<JobFileDto> getAllFilesByJobAndStatus(String jobId, String status) {
		Status enumStatus = JobFileEntity.Status.valueOf(status);
		List<JobFileEntity> entities = fileRepository.findAllFilesByJobAndStatus(jobId, enumStatus);

		if (entities == null) {
			throw new RuntimeException("No Files found under this Job");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getFiles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<JobFileEntity> files = fileRepository.findAll(pageable);

		if (files.getContent() == null)
			throw new RuntimeException("No Files found under this Job");

		return finalizePageResponse(files);
	}

	@Override
	public Map<String, Object> getFilesByJob(String jobId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<JobFileEntity> files = fileRepository.findFilesByJob(jobId, pageable);

		if (files.getContent() == null)
			throw new RuntimeException("No Files found under this Job");

		return finalizePageResponse(files);
	}

	@Override
	public Map<String, Object> getFilesByJobAndStatus(String jobId, String status, int page, int size) {
		Status enumStatus = JobFileEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);

		Page<JobFileEntity> files = fileRepository.findFilesByJobAndStatus(jobId, enumStatus, pageable);

		if (files.getContent() == null)
			throw new RuntimeException("No Files found under this Job");

		return finalizePageResponse(files);
	}

	/*
	 * Refactoring needed 1. Fetch Jobs one request 2. Batch Processing 3.
	 * Transactional 4. Job exists validation
	 */
	@Override
	@Transactional
	public JobFileDto save(JobFileDto dto) {
		JobFileEntity entity = null;

		if (dto.getId() != null) {
			entity = fileRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No File found with ID: " + dto.getId());
		}

		JobFileEntity savedEntity = fileRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	@Transactional
	public void deleteFile(String id) {
		JobFileEntity job = fileRepository.getOne(id);

		if (job == null)
			throw new RuntimeException("No Files found");

		fileRepository.delete(job);
	}

	@Override
	public void batchSave(List<JobFileDto> dtos) {
		Set<JobFileEntity> entities = new HashSet<JobFileEntity>();

		for (JobFileDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, fileRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		fileRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		fileRepository.deleteByIdIn(ids);
	}

	/*
	 * ============================== Service Util Methods
	 * =================================
	 */
	private JobFileEntity convertDtoToEntity(JobFileDto dto, JobFileEntity entity) {
		if (entity == null) {
			entity = this.mapper.map(dto, JobFileEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			this.mapper.map(dto, entity);
		}

		if (dto.getJob() != null) {
			entity.setJob(jobRepository.getOne(dto.getJob().getId()));
		}

		return entity;
	}

	private Map<String, Object> finalizePageResponse(Page<JobFileEntity> response) {
		List<JobFileEntity> entities = response.getContent();
		List<JobFileDto> dtos = getDtosFromEntities(entities);

		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put("content", dtos);
		responseModel.put("currentPage", response.getNumber());
		responseModel.put("totalItems", response.getTotalElements());
		responseModel.put("totalPages", response.getTotalPages());
		return responseModel;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private JobFileDto convertEntityToDto(JobFileEntity entity) {
		JobFileDto response = this.mapper.map(entity, JobFileDto.class);
		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

	private List<JobFileDto> getDtosFromEntities(List<JobFileEntity> entities) {
		List<JobFileDto> dtos = new ArrayList<JobFileDto>();

		for (JobFileEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
