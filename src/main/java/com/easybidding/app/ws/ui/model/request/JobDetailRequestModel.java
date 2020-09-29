package com.easybidding.app.ws.ui.model.request;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobDetailRequestModel {

	private String id;
	private String jobTitle;
	private String jobDescription;
	private String customNotes;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date datePublished;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date biddingDeadline;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date submissionDeadline;
	private String status;
	private CountyRequestModel county;
	private StateRequestModel state;
	private CountryRequestModel country;
	private List<AccountDetailRequestModel> accounts;
	private List<JobFileRequestModel> files;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getCustomNotes() {
		return customNotes;
	}

	public void setCustomNotes(String customNotes) {
		this.customNotes = customNotes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public Date getBiddingDeadline() {
		return biddingDeadline;
	}

	public void setBiddingDeadline(Date biddingDeadline) {
		this.biddingDeadline = biddingDeadline;
	}

	public Date getSubmissionDeadline() {
		return submissionDeadline;
	}

	public void setSubmissionDeadline(Date submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
	}

	public CountyRequestModel getCounty() {
		return county;
	}

	public void setCounty(CountyRequestModel county) {
		this.county = county;
	}

	public StateRequestModel getState() {
		return state;
	}

	public void setState(StateRequestModel state) {
		this.state = state;
	}

	public CountryRequestModel getCountry() {
		return country;
	}

	public void setCountry(CountryRequestModel country) {
		this.country = country;
	}

	public List<AccountDetailRequestModel> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountDetailRequestModel> accounts) {
		this.accounts = accounts;
	}

	public List<JobFileRequestModel> getFiles() {
		return files;
	}

	public void setFiles(List<JobFileRequestModel> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "Job{" + "Title = '" + jobTitle + '\'' + ", Accounts = '"
				+ accounts.stream().map(AccountDetailRequestModel::getId).collect(Collectors.toList()) + '\''
				+ ", Files = '" + files.stream().map(JobFileRequestModel::getId).collect(Collectors.toList()) + '\''
				+ ", Files = '" + files.stream().map(JobFileRequestModel::getFileName).collect(Collectors.toList())
				+ '\'' + files.stream().map(JobFileRequestModel::getFilePath).collect(Collectors.toList()) + '\'' + '}';
	}

}
