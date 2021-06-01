package com.easybidding.app.ws.ui.model.request;

public class JobCustomFieldRequestModel {

	private String id;
	private String fieldName;
	private String fieldValue;
	private JobDetailRequestModel job;
	private String status;

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

	public JobDetailRequestModel getJob() {
		return job;
	}

	public void setJob(JobDetailRequestModel job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
