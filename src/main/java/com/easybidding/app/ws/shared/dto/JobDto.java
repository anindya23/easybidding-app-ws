package com.easybidding.app.ws.shared.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

public class JobDto {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String jobTitle;
	private String jobDescription;
	private String address;
	private String city;
	private String county;
	private String postCode;
	private StateDto state;
	private CountryDto country;
	private String datePublished;
	private String biddingDeadline;
	private String submissionDeadline;
	private String status;
	private Date dateCreated;
	private Date dateLastUpdated;

	private Set<AccountDto> accounts = new HashSet<AccountDto>();
	private MultipartFile[] uploads;
	private List<JobFileDto> files;
	private List<JobCustomNoteDto> customNotes;
	private List<JobCustomFieldDto> fields;

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

	public List<JobCustomNoteDto> getCustomNotes() {
		return customNotes;
	}

	public void setCustomNotes(List<JobCustomNoteDto> customNotes) {
		this.customNotes = customNotes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto country) {
		this.country = country;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public void addAccount(AccountDto account) {
		this.accounts.add(account);
		account.getJobs().add(this);
	}

	public void removeAccount(AccountDto account) {
		this.accounts.remove(account);
		account.getJobs().remove(this);
	}

	public Set<AccountDto> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountDto> accounts) {
		this.accounts = accounts;
	}

	public MultipartFile[] getUploads() {
		return uploads;
	}

	public void setUploads(MultipartFile[] uploads) {
		this.uploads = uploads;
	}

	public List<JobFileDto> getFiles() {
		return files;
	}

	public void setFiles(List<JobFileDto> files) {
		this.files = files;
	}

	public List<JobCustomFieldDto> getFields() {
		return fields;
	}

	public void setFields(List<JobCustomFieldDto> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "Job{" + "Title = '" + jobTitle + '\'' + ", Accounts = '"
				+ accounts.stream().map(AccountDto::getId).collect(Collectors.toList()) + '\'' + ", Files = '"
				+ files.stream().map(JobFileDto::getId).collect(Collectors.toList()) + '\'' + ", Files = '"
				+ files.stream().map(JobFileDto::getFileName).collect(Collectors.toList()) + '\''
				+ files.stream().map(JobFileDto::getFilePath).collect(Collectors.toList()) + '\''
				+ fields.stream().map(JobCustomFieldDto::getFieldName).collect(Collectors.toList()) + '\''
				+ ", Files = '" + fields.stream().map(JobCustomFieldDto::getFieldValue).collect(Collectors.toList())
				+ '\'' + '}';
	}

}
