package com.easybidding.app.ws.ui.model.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class JobDetailResponseModel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String jobTitle;
	private String jobDescription;
	private String customNotes;
	private String datePublished;
	private String biddingDeadline;
	private String submissionDeadline;
	private String status;
	private String dateCreated;
	private String dateLastUpdated;
	private CountyResponseModel county;
	private StateResponseModel state;
	private CountryResponseModel country;
	private List<AccountDetailResponseModel> accounts;
//	private List<JobFileResponseModel> files;

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

	public String getCustomNotes() {
		return customNotes;
	}

	public void setCustomNotes(String customNotes) {
		this.customNotes = customNotes;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished, String timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.datePublished = dateFormat.format(datePublished);
	}

	public String getBiddingDeadline() {
		return biddingDeadline;
	}

	public void setBiddingDeadline(String biddingDeadline) {
		this.biddingDeadline = biddingDeadline;
	}

	public String getSubmissionDeadline() {
		return submissionDeadline;
	}

	public void setSubmissionDeadline(String submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
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

	public CountyResponseModel getCounty() {
		return county;
	}

	public void setCounty(CountyResponseModel county) {
		this.county = county;
	}

	public StateResponseModel getState() {
		return state;
	}

	public void setState(StateResponseModel state) {
		this.state = state;
	}

	public CountryResponseModel getCountry() {
		return country;
	}

	public void setCountry(CountryResponseModel country) {
		this.country = country;
	}

	public List<AccountDetailResponseModel> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountDetailResponseModel> accounts) {
		this.accounts = accounts;
	}

//	public List<JobFileResponseModel> getFiles() {
//		return files;
//	}
//
//	public void setFiles(List<JobFileResponseModel> files) {
//		this.files = files;
//	}

	@Override
	public String toString() {
		return "Job{" + "Title = '" + jobTitle + '\'' + ", Accounts = '"
				+ accounts.stream().map(AccountDetailResponseModel::getId).collect(Collectors.toList()) + '\''
//				+ ", Files = '" + files.stream().map(JobFileResponseModel::getId).collect(Collectors.toList()) + '\''
//				+ ", Files = '" + files.stream().map(JobFileResponseModel::getFileName).collect(Collectors.toList())
//				+ '\'' + files.stream().map(JobFileResponseModel::getFilePath).collect(Collectors.toList()) + '\''
				+ '}';
	}

}