package com.easybidding.app.ws.ui.model.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SystemRoleResponseModel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;
	private String roleCode;
	private String roleName;
	private String roleDescription;
	private String status;
	private String dateCreated;
	private String dateLastUpdated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

}
