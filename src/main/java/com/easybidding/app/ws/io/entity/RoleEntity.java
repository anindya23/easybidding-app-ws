package com.easybidding.app.ws.io.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "eb_roles")
public class RoleEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -145077322530745894L;

	public enum Status {
		ACTIVE, INACTIVE, DELETED
	}

	@Column(length = 10)
	private String roleCode;

	@Column(nullable = false, length = 255)
	private String roleName;

	@Column(columnDefinition = "TEXT")
	private String roleDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private AccountEntity account;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'DELETED')")
	private Status status;

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

	@ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Set<UserEntity> users = new HashSet<UserEntity>();
	
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public void addUser(UserEntity user) {
		this.users.add(user);
		user.getRoles().add(this);
	}

	public void removeUser(UserEntity user) {
		this.users.remove(user);
		user.getRoles().remove(this);
	}
	
	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

}
