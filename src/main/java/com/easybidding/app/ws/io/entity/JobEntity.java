package com.easybidding.app.ws.io.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "eb_jobs")
@SelectBeforeUpdate
public class JobEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 261898390264151141L;

	public enum Status {
		POSTED, INPROGRESS, COMPLETED
	}

	@Column(nullable = false, length = 255)
	private String jobTitle;

	@Column(columnDefinition = "TEXT")
	private String jobDescription;

//	@Column(columnDefinition = "TEXT")
//	private String customNotes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "county_id")
	private CountyEntity county;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	private StateEntity state;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	private CountryEntity country;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('POSTED', 'INPROGRESS', 'COMPLETED')")
	private Status status = Status.POSTED;

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "date_published")
//	private Date datePublished;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "bidding_deadline")
//	private Date biddingDeadline;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "submission_deadline")
//	private Date submissionDeadline;

	@Column(name = "date_published")
	private String datePublished;

	@Column(name = "bidding_deadline")
	private String biddingDeadline;

	@Column(name = "submission_deadline")
	private String submissionDeadline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private UserEntity createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_updated_by")
	private UserEntity lastUpdatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_updated")
	private Date dateLastUpdated = new Date();

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
	@JoinTable(name = "eb_account_jobs", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
	private Set<AccountEntity> accounts = new HashSet<AccountEntity>();

	@OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST ,CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT) // Otherwise Child (File here) remove is not working. Need to study further. 
	private List<JobFileEntity> files;
	
	@OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST ,CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<JobCustomFieldEntity> fields;
	
	@OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST ,CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<JobCustomNoteEntity> customNotes;

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

//	public String getCustomNotes() {
//		return customNotes;
//	}
//
//	public void setCustomNotes(String customNotes) {
//		this.customNotes = customNotes;
//	}

	public List<JobCustomNoteEntity> getCustomNotes() {
		return customNotes;
	}

	public void setCustomNotes(List<JobCustomNoteEntity> customNotes) {
		this.customNotes = customNotes;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public CountyEntity getCounty() {
		return county;
	}

	public void setCounty(CountyEntity countyEntity) {
		this.county = countyEntity;
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(StateEntity state) {
		this.state = state;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

//	public Date getDatePublished() {
//		return datePublished;
//	}
//
//	public void setDatePublished(Date datePublished) {
//		this.datePublished = datePublished;
//	}
//
//	public Date getBiddingDeadline() {
//		return biddingDeadline;
//	}
//
//	public void setBiddingDeadline(Date biddingDeadline) {
//		this.biddingDeadline = biddingDeadline;
//	}
//
//	public Date getSubmissionDeadline() {
//		return submissionDeadline;
//	}
//
//	public void setSubmissionDeadline(Date submissionDeadline) {
//		this.submissionDeadline = submissionDeadline;
//	}

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

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public void addAccount(AccountEntity account) {
		this.accounts.add(account);
		account.getJobs().add(this);
	}

	public void removeAccount(AccountEntity account) {
		this.accounts.remove(account);
		account.getJobs().remove(this);
	}

	public Set<AccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountEntity> accounts) {
		this.accounts = accounts;
	}

	public List<JobFileEntity> getFiles() {
		return files;
	}

	public void setFiles(List<JobFileEntity> files) {
		this.files = files;
	}

	public List<JobCustomFieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<JobCustomFieldEntity> fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		return "Job{" + "Title = '" + jobTitle + '\'' + ", Accounts = '"
				+ accounts.stream().map(AccountEntity::getId).collect(Collectors.toList()) + '\'' + '}';
	}

}
