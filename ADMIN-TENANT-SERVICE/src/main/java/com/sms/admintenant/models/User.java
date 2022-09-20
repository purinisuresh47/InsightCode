package com.sms.admintenant.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * User model class
 * 
 * @author Siva
 *
 */
@Entity
@Table(name = "userMasterCopy")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Lob
	@Column(name = "userImage")
	private String userImage;

	@Column(name = "email")
	private String email;

	@Column(name = "mobilenumber")
	private String mobilenumber;

	@Column(name = "extensionNumber")
	private String extensionNumber;

	@Column(name = "rolealias")
	private String rolealias;

	@Column(name = "clientmaster")
	private String clientmaster;

	@Column(name = "siteadmin")
	private String siteadmin;

	@Column(name = "siteuser")
	private String siteuser;

	@Column(name = "username")
	private String username;

	@Column(name = "organizationName")
	private String organizationName;

	@Column(name = "password")
	private String password;

	@Column(name = "passwordConfirm")
	private String passwordConfirm;

	@Column(name = "country")
	private String country;

	@Column(name = "state")
	private String state;

	@Column(name = "location")
	private String location;

	@Column(name = "city")
	private String city;

	@Column(name = "isVerified")
	private boolean isVerified;

	@Column(name = "authorities")
	private String authorities;

	@Column(name = "role")
	private String role;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "confirmationToken")
	private String confirmationToken;

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getRolealias() {
		return rolealias;
	}

	public void setRolealias(String rolealias) {
		this.rolealias = rolealias;
	}
	
	public String getClientmaster() {
		return clientmaster;
	}

	public void setClientmaster(String clientmaster) {
		this.clientmaster = clientmaster;
	}

	public String getSiteadmin() {
		return siteadmin;
	}

	public void setSiteadmin(String siteadmin) {
		this.siteadmin = siteadmin;
	}

	public String getSiteuser() {
		return siteuser;
	}

	public void setSiteuser(String siteuser) {
		this.siteuser = siteuser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
