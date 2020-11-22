package com.easybidding.app.ws.shared.dto;

import org.springframework.web.multipart.MultipartFile;

public class JobFilesDto {

	private String[] accounts;

	private String jobId;

	private MultipartFile[] files;

	public String[] getAccounts() {
		return accounts;
	}

	public void setAccounts(String[] accounts) {
		this.accounts = accounts;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

}
