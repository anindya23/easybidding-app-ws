package com.easybidding.app.ws.shared.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JobDto {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private String id;
	private String jobTitle;
	private String jobDescription;
	private String customNotes;
	private CountyDto county;
	private StateDto state;
	private CountryDto country;
	private String datePublished;
	private String biddingDeadline;
	private String submissionDeadline;
	private String status;
//	private UserDto createdBy;
//	private UserDto lastUpdatedBy;
	private Date dateCreated;
	private Date dateLastUpdated;

	private Set<AccountDto> accounts = new HashSet<AccountDto>();
	private List<JobFileDto> files;

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

	public CountyDto getCounty() {
		return county;
	}

	public void setCounty(CountyDto county) {
		this.county = county;
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

	public Date getDatePublished() {
		Date d = null;
		if (datePublished != null) {
			try {
				d = format.parse(datePublished);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}

	public Date getBiddingDeadline() {
		Date d = null;
		if (biddingDeadline != null) {
			try {
				d = format.parse(biddingDeadline);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public void setBiddingDeadline(String biddingDeadline) {
		this.biddingDeadline = biddingDeadline;
	}

	public Date getSubmissionDeadline() {
		Date d = null;
		if (submissionDeadline != null) {
			try {
				d = format.parse(submissionDeadline);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
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

	public List<JobFileDto> getFiles() {
		return files;
	}

	public void setFiles(List<JobFileDto> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "Job{" + "Title = '" + jobTitle + '\'' + ", Accounts = '"
				+ accounts.stream().map(AccountDto::getId).collect(Collectors.toList()) + '\'' + ", Files = '"
				+ files.stream().map(JobFileDto::getId).collect(Collectors.toList()) + '\'' + ", Files = '"
				+ files.stream().map(JobFileDto::getFileName).collect(Collectors.toList()) + '\''
				+ files.stream().map(JobFileDto::getFilePath).collect(Collectors.toList()) + '\'' + '}';
	}

}
