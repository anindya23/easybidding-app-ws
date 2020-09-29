package com.easybidding.app.ws.ui.model.request;

public class JobFileRequestModel {

	private String id;
	private String fileName;
	private String filePath;
	private JobDetailRequestModel job;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
