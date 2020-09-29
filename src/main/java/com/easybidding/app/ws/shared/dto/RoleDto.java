package com.easybidding.app.ws.shared.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RoleDto {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String roleCode;
	private String roleName;
	private String roleDescription;
	private AccountDto account;
	private String status;
	private UserDto createdBy;
	private UserDto lastUpdatedBy;
	private String dateCreated;
	private String dateLastUpdated;

	public String getRoleCode() {
		return roleCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDto createdBy) {
		this.createdBy = createdBy;
	}

	public UserDto getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserDto lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateLastUpdated(String dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
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

}
