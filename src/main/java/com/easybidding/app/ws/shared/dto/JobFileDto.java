package com.easybidding.app.ws.shared.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JobFileDto {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String fileId;
	private String fileName;
	private String filePath;
	private JobDto job;
	private String status;
	private UserDto createdBy;
	private UserDto lastUpdatedBy;
	private String dateCreated;
	private String dateLastUpdated;

	public String getFileId() {
		return fileId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public JobDto getJob() {
		return job;
	}

	public void setJob(JobDto job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDto createdBy) {
		this.createdBy = createdBy;
	}

	public UserDto getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserDto lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateLastUpdated(String dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateCreated = dateFormat.format(dateCreated);
	}

	public String getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateLastUpdated = dateFormat.format(dateLastUpdated);
	}

}
