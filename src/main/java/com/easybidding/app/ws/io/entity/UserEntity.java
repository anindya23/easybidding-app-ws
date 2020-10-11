package com.easybidding.app.ws.io.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "eb_users")
@SelectBeforeUpdate
public class UserEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4717660078517683703L;

	public enum Status {
		ACTIVE, INACTIVE, DELETED
	}

	@Column(nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 255)
	private String firstName;

	@Column(length = 255)
	private String lastName;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(length = 255)
	private String city;

	@Column(length = 255)
	private String district;

	@Column(length = 50)
	private String postCode;

	@Column(length = 50)
	private String telephone;

	@Column(length = 50)
	private String mobile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private AccountEntity account;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "eb_user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<RoleEntity>();

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "system_role")
//	private SystemRoleEntity systemRole;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "county_id")
	private CountyEntity county;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id")
	private StateEntity state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private CountryEntity country;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_active")
	private Date dateLastActive = new Date();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void addRole(RoleEntity role) {
		this.roles.add(role);
		role.getUsers().add(this);
	}

	public void removeRole(RoleEntity role) {
		this.roles.remove(role);
		role.getUsers().remove(this);
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public List<String> getRoleCodes() {
		List<String> roleCodes = new ArrayList<String>();
		for (RoleEntity role : getRoles())
			roleCodes.add(role.getRoleCode());
		return roleCodes;
	}
	
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public CountyEntity getCounty() {
		return county;
	}

	public void setCounty(CountyEntity county) {
		this.county = county;
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

	public Date getDateLastActive() {
		return dateLastActive;
	}

	public void setDateLastActive(Date dateLastActive) {
		this.dateLastActive = dateLastActive;
	}

}
