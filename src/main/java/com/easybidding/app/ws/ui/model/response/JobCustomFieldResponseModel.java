package com.easybidding.app.ws.ui.model.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JobCustomFieldResponseModel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String fieldName;
	private String fieldValue;
	private String status;
	private String dateCreated;
	private String dateLastUpdated;
	private JobDetailResponseModel job;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fieldName;
	}

	public void setFileName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFilePath() {
		return fieldValue;
	}

	public void setFilePath(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public JobDetailResponseModel getJob() {
		return job;
	}

	public void setJob(JobDetailResponseModel job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
