package com.easybidding.app.ws.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.io.entity.JobEntity;
import com.easybidding.app.ws.io.entity.JobEntity.Status;
import com.easybidding.app.ws.io.entity.JobFileEntity;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.JobService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.shared.dto.JobDto;
import com.easybidding.app.ws.shared.dto.JobFileDto;

@Service
public class JobServiceImpl implements JobService {

//	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobFileRepository fileRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountyRepository countyRepository;

	@Autowired
	Utils utils;

	@PersistenceContext
	EntityManager em;

	private ModelMapper mapper;

	@Autowired
	public JobServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		this.mapper.addMappings(mapping);
		this.mapper.addMappings(dtoMapping);
	}

	PropertyMap<JobDto, JobEntity> mapping = new PropertyMap<JobDto, JobEntity>() {
		protected void configure() {
			skip().setAccounts(null);
			skip().setFiles(null);
			skip().setCountry(null);
			skip().setState(null);
			skip().setCounty(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<JobEntity, JobDto> dtoMapping = new PropertyMap<JobEntity, JobDto>() {
		protected void configure() {
			skip().setFiles(null);
			skip().setAccounts(null);
		}
	};

	@Override
	public JobDto getJobById(String jobId) {
		JobEntity entity = jobRepository.getOne(jobId);

		if (entity == null)
			throw new RuntimeException("No Jobs found");

		return convertEntityToDto(entity);
	}

	@Override
	public List<JobDto> getAllJobs() {
		List<JobEntity> entities = jobRepository.findAll();

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getJobs(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<JobEntity> jobs = jobRepository.findAll(pageable);

		if (jobs.getContent() == null)
			throw new RuntimeException("No Jobs found under this account");

		return finalizePageResponse(jobs);
	}

	@Override
	public List<JobDto> getAllJobsByAccount(String accountId) {
		List<JobEntity> entities = jobRepository.findAllJobsByAccount(accountId);

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public List<JobDto> getAllJobsByAccountAndStatus(String accountId, String status) {
		Status enumStatus = JobEntity.Status.valueOf(status);
		List<JobEntity> entities = jobRepository.findAllJobsByAccountAndStatus(accountId, enumStatus);

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public Map<String, Object> getJobsByAccount(String accountId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<JobEntity> jobs = jobRepository.findJobsByAccount(accountId, pageable);

		if (jobs.getContent() == null)
			throw new RuntimeException("No Jobs found under this account");

		return finalizePageResponse(jobs);
	}

	@Override
	public Map<String, Object> getJobsByAccountAndStatus(String accountId, String status, int page, int size) {
		Status enumStatus = JobEntity.Status.valueOf(status);
		Pageable pageable = PageRequest.of(page, size);

		Page<JobEntity> jobs = jobRepository.findJobsByAccountAndStatus(accountId, enumStatus, pageable);

		if (jobs.getContent() == null)
			throw new RuntimeException("No Jobs found under this account");

		return finalizePageResponse(jobs);
	}

	/*
	 * Refactoring needed 1. Fetch Accounts one request 2. Batch Processing 3.
	 * Transactional 4. Account exists validation
	 */
	@Override
	@Transactional
	public JobDto save(JobDto dto) {
		JobEntity entity = null;

		if (dto.getId() != null) {
			entity = jobRepository.getOne(dto.getId());

			if (entity == null)
				throw new RuntimeException("No Job found with ID: " + dto.getId());
		}

		JobEntity savedEntity = jobRepository.save(convertDtoToEntity(dto, entity));
		return convertEntityToDto(savedEntity);
	}

	@Override
	@Transactional
	public void deleteJob(String id) {
		JobEntity job = jobRepository.getOne(id);

		if (job == null)
			throw new RuntimeException("No Jobs found");

		jobRepository.delete(job);
	}

	@Override
	public void batchSave(List<JobDto> dtos) {
		Set<JobEntity> entities = new HashSet<JobEntity>();

		for (JobDto dto : dtos) {
			if (dto.getId() != null)
				entities.add(convertDtoToEntity(dto, jobRepository.getOne(dto.getId())));
			else
				entities.add(convertDtoToEntity(dto, null));
		}

		jobRepository.saveInBatch(entities);
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		jobRepository.deleteByIdIn(ids);
	}

	/*
	 * Refactoring 1. Conversion shouldn't be here. It's Not Service Layer's
	 * responsibility. It's the duty of the layer which is going to use Service
	 * Layer.
	 */
	private JobEntity convertDtoToEntity(JobDto dto, JobEntity entity) {
		if (dto.getId() == null) {
			entity = this.mapper.map(dto, JobEntity.class);
			entity.setId(utils.generateUniqueId(30));
		} else {
			entity = jobRepository.getOne(dto.getId());
			this.mapper.map(dto, entity);
		}

		if (dto.getAccounts() != null && !dto.getAccounts().isEmpty()) {
			List<String> ids = new ArrayList<String>();
			for (AccountDto accountDto : dto.getAccounts())
				ids.add(accountDto.getId());
			entity.setAccounts(accountRepository.findAccountsByIds(ids));
		}

		if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
			List<JobFileEntity> files = new ArrayList<JobFileEntity>();
			for (JobFileDto fileDto : dto.getFiles()) {
				if (fileDto.getId() == null) {
					fileDto.setId(utils.generateUniqueId(30));
				}
				files.add(this.mapper.map(fileDto, JobFileEntity.class));
			}
			entity.setFiles(files);
		}

		if (dto.getCountry() != null) {
			entity.setCountry(countryRepository.findByCountryCode(dto.getCountry().getCountryCode()));
		}

		if (dto.getState() != null && dto.getCountry() != null) {
			entity.setState(
					stateRepository.findByStateCode(dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		if (dto.getCounty() != null && dto.getState() != null && dto.getCountry() != null) {
			entity.setCounty(countyRepository.findByCountyCode(dto.getCounty().getCountyCode(),
					dto.getState().getStateCode(), dto.getCountry().getCountryCode()));
		}

		return entity;
	}

	/*
	 * Refactoring is needed in this method 1. Hardcoded timezone should be replaced
	 * by Logged In User's Timezone 2. The whole method should be placed in
	 * BaseEntity Class
	 */
	private JobDto convertEntityToDto(JobEntity entity) {
		JobDto response = this.mapper.map(entity, JobDto.class);
//		response.setDateCreated(entity.getDateCreated(), "Asia/Dhaka");
//		response.setDateLastUpdated(entity.getDateLastUpdated(), "Asia/Dhaka");
		return response;
	}

	private List<JobDto> getDtosFromEntities(List<JobEntity> entities) {
		List<JobDto> dtos = new ArrayList<JobDto>();

		for (JobEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

	private Map<String, Object> finalizePageResponse(Page<JobEntity> response) {
		List<JobEntity> entities = response.getContent();
		List<JobDto> dtos = getDtosFromEntities(entities);

		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put("content", dtos);
		responseModel.put("currentPage", response.getNumber());
		responseModel.put("totalItems", response.getTotalElements());
		responseModel.put("totalPages", response.getTotalPages());
		return responseModel;
	}

}
