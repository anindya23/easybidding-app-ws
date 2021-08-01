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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.easybidding.app.ws.io.entity.AccountEntity;
import com.easybidding.app.ws.io.entity.JobAccountEntity;
import com.easybidding.app.ws.io.entity.JobAccountEntity.Status;
import com.easybidding.app.ws.io.entity.JobCustomFieldEntity;
import com.easybidding.app.ws.io.entity.JobCustomNoteEntity;
import com.easybidding.app.ws.io.entity.JobEntity;
import com.easybidding.app.ws.io.entity.JobFileEntity;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.CountryRepository;
import com.easybidding.app.ws.repository.impl.CountyRepository;
import com.easybidding.app.ws.repository.impl.JobAccountRepository;
import com.easybidding.app.ws.repository.impl.JobCustomFieldRepository;
import com.easybidding.app.ws.repository.impl.JobCustomNoteRepository;
import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.repository.impl.StateRepository;
import com.easybidding.app.ws.service.JobFileService;
import com.easybidding.app.ws.service.JobService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.shared.dto.JobAccountDto;
import com.easybidding.app.ws.shared.dto.JobCustomFieldDto;
import com.easybidding.app.ws.shared.dto.JobCustomNoteDto;
import com.easybidding.app.ws.shared.dto.JobDto;
import com.easybidding.app.ws.shared.dto.JobFileDto;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobAccountRepository jobAccountRepository;

	@Autowired
	JobCustomNoteRepository noteRepository;

	@Autowired
	JobCustomFieldRepository fieldRepository;

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
	JobFileService jobFileService;

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
			skip().setJobAccounts(null);
			skip().setFiles(null);
			skip().setFields(null);
			skip().setCustomNotes(null);
			skip().setCountry(null);
			skip().setState(null);
			skip().setCounty(null);
			skip().setDateCreated(null);
			skip().setDateLastUpdated(null);
		}
	};

	PropertyMap<JobEntity, JobDto> dtoMapping = new PropertyMap<JobEntity, JobDto>() {
		protected void configure() {
			skip().setAccounts(null);
			skip().setFiles(null);
			skip().setFields(null);
			skip().setCustomNotes(null);
			skip().setUploads(null);
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
	public List<JobAccountDto> getAllJobsByAccountAndStatus(String accountId, String status) {
		Set<JobAccountEntity> entities = jobAccountRepository.findAllJobsByAccountAndStatus(accountId,
				Status.valueOf(status));

		if (entities == null) {
			throw new RuntimeException("No Jobs found under this account");
		}
		return getJobAccountDtosFromEntities(entities);
	}

	@Override
	@Transactional
	public JobAccountDto changeJobStatus(JobAccountDto jobAccount) {
		JobAccountEntity entity = jobAccountRepository.getOne(jobAccount.getId());

		if (entity == null)
			throw new RuntimeException("No Job found with ID: " + jobAccount.getId());

		entity.setStatus(Status.valueOf(jobAccount.getStatus()));

		JobAccountEntity savedEntity = jobAccountRepository.save(entity);
		return convertJobAccountEntityToDto(savedEntity);
	}

	@Override
	@Transactional
	public void removeAccountJob(String id) {
		JobAccountEntity entity = jobAccountRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("No Record found");

		String key = "jobs/" + entity.getJob().getId() + "/" + entity.getAccount().getId();
		
		if (amazonS3.doesObjectExist(bucket, key)) {
			deleteDirectory(key);	
		}

		JobEntity job = jobRepository.getOne(entity.getJob().getId());
		AccountEntity account = accountRepository.getOne(entity.getAccount().getId());
		job.getJobAccounts().remove(entity);
		account.getJobAccounts().remove(entity);
		jobRepository.save(job);
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

	public void deleteDirectory(String prefix) {
	    ObjectListing objectList = amazonS3.listObjects( bucket, prefix );
	    List<S3ObjectSummary> objectSummeryList =  objectList.getObjectSummaries();
	    String[] keysList = new String[ objectSummeryList.size() ];
	    int count = 0;
	    for( S3ObjectSummary summery : objectSummeryList ) {
	        keysList[count++] = summery.getKey();
	    }
	    DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest( bucket ).withKeys( keysList );
	    amazonS3.deleteObjects(deleteObjectsRequest);
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

			for (AccountDto accountDto : dto.getAccounts()) {
				ids.add(accountDto.getId());
			}

			Set<JobAccountEntity> jobAccounts = new HashSet<JobAccountEntity>();
			if (dto.getId() == null) {
				for (AccountEntity accountEntity : accountRepository.findAccountsByIds(ids)) {
					JobAccountEntity jobAccountEntity = new JobAccountEntity();
					jobAccountEntity.setId(utils.generateUniqueId(30));
					jobAccountEntity.setAccount(accountEntity);
					jobAccountEntity.setJob(entity);
					jobAccounts.add(jobAccountEntity);
				}
				entity.setJobAccounts(jobAccounts);
			} else {
				for (JobAccountEntity jobAccount : entity.getJobAccounts()) {
					if (ids.contains(jobAccount.getAccount().getId())) {
						jobAccounts.add(jobAccount);
					}
				}
			}

			if (dto.getId() == null) {
				entity.setJobAccounts(jobAccounts);
			} else {
				entity.getJobAccounts().clear();
				entity.getJobAccounts().addAll(jobAccounts);
			}
		}

		if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
			List<JobFileEntity> files = new ArrayList<JobFileEntity>();
			for (JobFileDto fileDto : dto.getFiles()) {
				if (fileDto.getId() == null) {
					fileDto.setId(utils.generateUniqueId(30));
				}
				JobFileEntity fileEntity = this.mapper.map(fileDto, JobFileEntity.class);
				fileEntity.setJob(entity);
				files.add(fileEntity);
			}
			entity.setFiles(files);
		}

		if (dto.getCustomNotes() != null && !dto.getCustomNotes().isEmpty()) {
			List<JobCustomNoteEntity> notes = new ArrayList<JobCustomNoteEntity>();

			for (JobCustomNoteDto noteDto : dto.getCustomNotes()) {
				if (noteDto.getId() == null) {
					JobCustomNoteEntity noteEntity = new JobCustomNoteEntity();
					noteEntity.setId(utils.generateUniqueId(30));
					noteEntity.setNote(noteDto.getNote());
					noteEntity.setAccount(accountRepository.getOne(noteDto.getAccount().getId()));
					noteEntity.setJob(jobRepository.getOne(noteDto.getJob().getId()));
					notes.add(noteEntity);
				} else {
					for (JobCustomNoteEntity noteEntity : entity.getCustomNotes()) {
						if (noteDto.getAccount().getId().equals(noteEntity.getAccount().getId())) {
							noteEntity.setNote(noteDto.getNote());
						}
						notes.add(noteEntity);
					}
				}
			}

			if (dto.getId() == null) {
				entity.setCustomNotes(notes);
			} else {
				entity.getCustomNotes().clear();
				entity.getCustomNotes().addAll(notes);
			}
		}

		if (dto.getFields() != null && !dto.getFields().isEmpty()) {
			List<JobCustomFieldEntity> fields = new ArrayList<JobCustomFieldEntity>();

			for (JobCustomFieldDto fieldDto : dto.getFields()) {
				if (fieldDto.getId() == null) {
					JobCustomFieldEntity fieldEntity = new JobCustomFieldEntity();
					fieldEntity.setId(utils.generateUniqueId(30));
					fieldEntity.setAccount(accountRepository.getOne(fieldDto.getAccount().getId()));
					fieldEntity.setJob(entity);
					fieldEntity.setFieldName(fieldDto.getFieldName());
					fieldEntity.setFieldValue(fieldDto.getFieldValue());
					fields.add(fieldEntity);
				} else {
					for (JobCustomFieldEntity fieldEntity : entity.getFields()) {
						if (fieldEntity.getAccount().getId().equals(fieldDto.getAccount().getId())) {
							fieldEntity.setFieldName(fieldDto.getFieldName());
							fieldEntity.setFieldValue(fieldDto.getFieldValue());
						}
						fields.add(fieldEntity);
					}
				}
			}

			if (dto.getId() == null) {
				entity.setFields(fields);
			} else {
				entity.getFields().clear();
				entity.getFields().addAll(fields);
			}
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

		if (entity.getJobAccounts() != null && !entity.getJobAccounts().isEmpty()) {
			Set<AccountDto> accountDtos = new HashSet<AccountDto>();
			for (JobAccountEntity jobAccountEntity : entity.getJobAccounts()) {
				AccountDto accountDto = new AccountDto();
				accountDto.setId(jobAccountEntity.getAccount().getId());
				accountDto.setAccountName(jobAccountEntity.getAccount().getAccountName());
				accountDto.setStatus(jobAccountEntity.getAccount().getStatus().toString());
				accountDtos.add(accountDto);
			}
			response.setAccounts(accountDtos);
		}

		if (entity.getFiles() != null && !entity.getFiles().isEmpty()) {
			Set<JobFileEntity> entities = new HashSet<JobFileEntity>(entity.getFiles());
			List<JobFileDto> files = new ArrayList<JobFileDto>();

			for (JobFileEntity fileEntity : entities) {
				JobFileDto fileDto = new JobFileDto();
				fileDto.setId(fileEntity.getId());
				fileDto.setFileName(fileEntity.getFileName());
				fileDto.setFilePath(fileEntity.getFilePath());

				if (fileEntity.getJob() != null) {
					JobDto jobDto = new JobDto();
					jobDto.setId(fileEntity.getJob().getId());
					jobDto.setJobTitle(fileEntity.getJob().getJobTitle());
					fileDto.setJob(jobDto);
				}

				if (fileEntity.getAccount() != null) {
					AccountDto accountDto = new AccountDto();
					accountDto.setId(fileEntity.getAccount().getId());
					accountDto.setAccountName(fileEntity.getAccount().getAccountName());
					fileDto.setAccount(accountDto);
				}
				files.add(fileDto);
			}
			response.setFiles(files);
		}

		if (entity.getCustomNotes() != null && !entity.getCustomNotes().isEmpty()) {
			Set<JobCustomNoteEntity> entities = new HashSet<JobCustomNoteEntity>(entity.getCustomNotes());
			List<JobCustomNoteDto> noteDtos = new ArrayList<JobCustomNoteDto>();

			for (JobCustomNoteEntity noteEntity : entities) {
				JobCustomNoteDto noteDto = new JobCustomNoteDto();
				noteDto.setId(noteEntity.getId());
				noteDto.setNote(noteEntity.getNote());

				if (noteEntity.getJob() != null) {
					JobDto jobDto = new JobDto();
					jobDto.setId(noteEntity.getJob().getId());
					jobDto.setJobTitle(noteEntity.getJob().getJobTitle());
					noteDto.setJob(jobDto);
				}

				if (noteEntity.getAccount() != null) {
					AccountDto accountDto = new AccountDto();
					accountDto.setId(noteEntity.getAccount().getId());
					accountDto.setAccountName(noteEntity.getAccount().getAccountName());
					noteDto.setAccount(accountDto);
				}
				noteDtos.add(noteDto);
			}
			response.setCustomNotes(noteDtos);
		}

		if (entity.getFields() != null && !entity.getFields().isEmpty()) {
			Set<JobCustomFieldEntity> entities = new HashSet<JobCustomFieldEntity>(entity.getFields());
			List<JobCustomFieldDto> fields = new ArrayList<JobCustomFieldDto>();

			for (JobCustomFieldEntity fieldEntity : entities) {
				JobCustomFieldDto fieldDto = new JobCustomFieldDto();
				fieldDto.setId(fieldEntity.getId());
				fieldDto.setFieldName(fieldEntity.getFieldName());
				fieldDto.setFieldValue(fieldEntity.getFieldValue());

				if (fieldEntity.getJob() != null) {
					JobDto jobDto = new JobDto();
					jobDto.setId(fieldEntity.getJob().getId());
					jobDto.setJobTitle(fieldEntity.getJob().getJobTitle());
					fieldDto.setJob(jobDto);
				}

				if (fieldEntity.getAccount() != null) {
					AccountDto accountDto = new AccountDto();
					accountDto.setId(fieldEntity.getAccount().getId());
					accountDto.setAccountName(fieldEntity.getAccount().getAccountName());
					fieldDto.setAccount(accountDto);
				}
				fields.add(fieldDto);
			}
			response.setFields(fields);
		}

		return response;
	}

	private List<JobDto> getDtosFromEntities(List<JobEntity> entities) {
		List<JobDto> dtos = new ArrayList<JobDto>();

		for (JobEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

	private JobAccountDto convertJobAccountEntityToDto(JobAccountEntity entity) {
		JobAccountDto response = new JobAccountDto();
		response.setId(entity.getId());
		response.setAccount(this.mapper.map(entity.getAccount(), AccountDto.class));
		response.setJob(this.mapper.map(entity.getJob(), JobDto.class));
		response.setStatus(entity.getStatus().toString());

		return response;
	}

	private List<JobAccountDto> getJobAccountDtosFromEntities(Set<JobAccountEntity> entities) {
		List<JobAccountDto> dtos = new ArrayList<JobAccountDto>();

		for (JobAccountEntity entity : entities)
			dtos.add(convertJobAccountEntityToDto(entity));

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
