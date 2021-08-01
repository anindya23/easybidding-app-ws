package com.easybidding.app.ws.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.easybidding.app.ws.io.entity.JobEntity;
import com.easybidding.app.ws.io.entity.JobFileEntity;
import com.easybidding.app.ws.io.entity.JobFileEntity.Status;
import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.JobFileRepository;
import com.easybidding.app.ws.repository.impl.JobRepository;
import com.easybidding.app.ws.service.FilesStorageService;
import com.easybidding.app.ws.service.JobFileService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.shared.dto.JobDto;
import com.easybidding.app.ws.shared.dto.JobFileDto;
import com.easybidding.app.ws.shared.dto.JobFilesDto;

@Service
public class JobFileServiceImpl implements JobFileService {

	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobFileRepository fileRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	Utils utils;

	@Autowired
	FilesStorageService fileStorageService;

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
		}
	};

	PropertyMap<JobFileEntity, JobFileDto> dtoMapping = new PropertyMap<JobFileEntity, JobFileDto>() {
		protected void configure() {
			skip().setJob(null);
			skip().setAccount(null);
		}
	};

	@Override
	public JobFileDto getFileById(String id) {
		JobFileEntity entity = fileRepository.getOne(id);

		if (entity == null)
			throw new RuntimeException("No File found");

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
		String key = null;
		JobFileEntity jobFile = fileRepository.getOne(id);

		if (jobFile == null)
			throw new RuntimeException("No Files found");

		if (jobFile.getAccount().getId() != null) {
			key = "jobs/" + jobFile.getJob().getId() + "/" + jobFile.getAccount().getId() + "/" + jobFile.getFileName();
		} else {
			key = "jobs/" + jobFile.getJob().getId() + "/" + jobFile.getFileName();
		}

		amazonS3.deleteObject(bucket, key);

		JobEntity job = jobRepository.getOne(jobFile.getJob().getId());
		job.getFiles().remove(jobFile);
		jobRepository.save(job);
	}

	@Override
	@Transactional
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

	@Override
	@Async
	public List<JobFileDto> uploadFiles(JobFilesDto dto) {
		List<JobFileEntity> entities = new ArrayList<JobFileEntity>();

		try {
			Arrays.asList(dto.getFiles()).stream().forEach(multipartFile -> {

				if (dto.getAccounts() != null) {
					Arrays.asList(dto.getAccounts()).stream().forEach(account -> {
						JobFileEntity entity = new JobFileEntity();
						entity.setId(utils.generateUniqueId(30));
						entity.setJob(jobRepository.getOne(dto.getJobId()));
						entity.setAccount(accountRepository.getOne(account));
						final File file = convertMultiPartFileToFile(multipartFile);
						entity.setFileName(uploadFileToS3Bucket(bucket, account, dto.getJobId(), file));
						file.delete();
						entities.add(entity);
					});
				} else {
					JobFileEntity entity = new JobFileEntity();
					entity.setId(utils.generateUniqueId(30));
					entity.setJob(jobRepository.getOne(dto.getJobId()));
					final File file = convertMultiPartFileToFile(multipartFile);
					entity.setFileName(uploadFileToS3Bucket(bucket, null, dto.getJobId(), file));
					file.delete();
					entities.add(entity);
				}
			});
			fileRepository.saveInBatch(entities);
		} catch (final AmazonServiceException ex) {
			logger.info("File upload is failed.");
			logger.error("Error= {} while uploading file.", ex.getMessage());
		}
		return getDtosFromEntities(entities);
	}

	@Override
	public byte[] getJobFile(String fileId) {
		JobFileEntity entity = fileRepository.getOne(fileId);

		String dir = "jobs/";

		if (entity.getJob().getId() != null) {
			dir = dir + entity.getJob().getId() + "/";
		}

		if (entity.getAccount().getId() != null) {
			dir = dir + entity.getAccount().getId() + "/";
		}
		dir = dir + entity.getFileName();
		return downloadFile(dir);
	}

	@Override
	public ResponseEntity<Resource> getAllFiles(HttpServletResponse response, String jobId, String accountId)
			throws IOException {
		String name = "attachment_" + new Date().getTime() + ".zip";
		fileToZip(jobId, accountId, name);
		Resource resource = fileStorageService.loadFileAsResource(name);
		String contentType = "application/octet-stream";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/*
	 * ============================== Service Util Methods
	 * =================================
	 */
	private String getS3Dir(String jobId, String accountId) {
		String s3Dir = "jobs/";

		if (jobId != null) {
			s3Dir = s3Dir + jobId + "/";
		}

		if (accountId != null) {
			s3Dir = s3Dir + accountId + "/";
		}
		return s3Dir;
	}

	private String fileToZip(String jobId, String accountId, String fileName) {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		String tempPath = System.getProperty("java.io.tmpdir");

		try {
			File zipFile = new File(tempPath, fileName);
			zipFile.deleteOnExit();
			zipFile.createNewFile();

			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));

			byte[] content = new byte[1024 * 10];

			String jobDir = getS3Dir(jobId, null);
			String accountDir = getS3Dir(jobId, accountId);
			List<S3ObjectSummary> objectSummaries = amazonS3.listObjects(bucket, jobDir).getObjectSummaries();

			for (S3ObjectSummary objectSummary : objectSummaries) {
				String s3Path = objectSummary.getKey().substring(0, objectSummary.getKey().lastIndexOf("/"));

				if (!jobDir.equals(s3Path + "/") && !accountDir.equals(s3Path + "/"))
					continue;

				S3Object obj = amazonS3.getObject(bucket, objectSummary.getKey());
				S3ObjectInputStream stream = obj.getObjectContent();
				bis = new BufferedInputStream(stream);

				ZipEntry zipEntry = new ZipEntry(objectSummary.getKey());
				zos.putNextEntry(zipEntry);

				int read = 0;
				while ((read = bis.read(content, 0, 1024 * 10)) != -1) {
					zos.write(content, 0, read);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return tempPath + "/" + fileName;
	}

	private byte[] downloadFile(String dir) {
		S3Object obj = amazonS3.getObject(bucket, dir);
		S3ObjectInputStream stream = obj.getObjectContent();

		try {
			byte[] content = IOUtils.toByteArray(stream);
			obj.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

	private String uploadFileToS3Bucket(final String bucket, String accountId, String jobId, final File file) {
		final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();

		String dir = "jobs/";

		if (jobId != null) {
			dir = dir + jobId + "/";
		}

		if (accountId != null) {
			dir = dir + accountId + "/";
		}
		dir = dir + uniqueFileName;

		amazonS3.putObject(new PutObjectRequest(bucket, dir, file));
		return uniqueFileName;
	}

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

	private List<JobFileDto> getDtosFromEntities(List<JobFileEntity> entities) {
		List<JobFileDto> dtos = new ArrayList<JobFileDto>();

		for (JobFileEntity entity : entities)
			dtos.add(convertEntityToDto(entity));

		return dtos;
	}

}
