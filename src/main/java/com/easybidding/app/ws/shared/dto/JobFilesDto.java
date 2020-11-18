package com.easybidding.app.ws.shared.dto;

import org.springframework.web.multipart.MultipartFile;

public class JobFilesDto {

	private String accountId;

	private String jobId;

	private MultipartFile[] files;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
