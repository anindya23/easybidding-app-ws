package com.easybidding.app.ws.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import com.easybidding.app.ws.shared.dto.JobFilesDto;

@Service
public class JobServiceImpl implements JobService {

	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;

	private final Path root = Paths.get("uploads");

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
//			skip().setAccounts(null);
		}
	};

	@Override
	public JobDto getJobById(String jobId) {
//		JobEntity entity = jobRepository.getOne(jobId);
		JobEntity entity = jobRepository.findJobById(jobId);

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

	@Override
	@Async
	public void uploadFiles(JobFilesDto dto) {
		Set<JobFileEntity> entities = new HashSet<JobFileEntity>();

		try {
			Arrays.asList(dto.getFiles()).stream().forEach(multipartFile -> {
				
				JobFileEntity entity = new JobFileEntity();
				entity.setId(utils.generateUniqueId(30));
				entity.setJob(jobRepository.getOne(dto.getJobId()));
				entity.setAccount(accountRepository.getOne(dto.getAccountId()));
				
				final File file = convertMultiPartFileToFile(multipartFile);
				entity.setFileName(uploadFileToS3Bucket(bucket, dto, file));
				
				file.delete();
				entities.add(entity);
			});
			fileRepository.saveInBatch(entities);
		} catch (final AmazonServiceException ex) {
			logger.info("File upload is failed.");
			logger.error("Error= {} while uploading file.", ex.getMessage());
		}
	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
		
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (final IOException ex) {
			logger.error("Error converting the multi-part file to file= ", ex.getMessage());
		}
		return file;
	}

	private String uploadFileToS3Bucket(final String bucket, JobFilesDto dto, final File file) {
		final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
		
		String dir = "jobs/";
		
		if (dto.getJobId() != null) {
			dir = dir + dto.getJobId() + "/";
		}
		
		if (dto.getAccountId() != null) {
			dir = dir + dto.getAccountId() + "/";
		}
		dir = dir + uniqueFileName;

		amazonS3.putObject(new PutObjectRequest(bucket, dir, file));
		return uniqueFileName;
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
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
//				files.add(this.mapper.map(fileDto, JobFileEntity.class));
				JobFileEntity fileEntity = this.mapper.map(fileDto, JobFileEntity.class);
				fileEntity.setJob(entity);
				files.add(fileEntity);
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

		if (entity.getFiles() != null && !entity.getFiles().isEmpty()) {
			List<JobFileDto> files = new ArrayList<JobFileDto>();
			for (JobFileEntity fileEntity : entity.getFiles()) {
				JobFileDto fileDto = new JobFileDto();
				fileDto.setId(fileEntity.getId());
				fileDto.setFileName(fileEntity.getFileName());
				fileDto.setFilePath(fileEntity.getFilePath());

				files.add(fileDto);
			}
			response.setFiles(files);
		}
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
