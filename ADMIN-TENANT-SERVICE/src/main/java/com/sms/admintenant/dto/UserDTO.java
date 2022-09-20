package com.sms.admintenant.dto;

import java.io.Serializable;

/**
 *  User DTO class
 *  
 * @author Siva
 *
 */
public class UserDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 91901774547107674L;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The email. */
	private String email;

	/** The mobilenumber. */
	private String mobilenumber;

	/** The password confirm. */
	private String passwordConfirm;
	
	private String rolealias;

	/** The role. */
	private String role;

	private String createdBy;
	
	private String clientmaster;
	private String siteadmin;
	private String siteuser;

	/**
	 * Instantiates a new user DTO.
	 */
	public UserDTO() {
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobilenumber.
	 *
	 * @return the mobilenumber
	 */
	public String getMobilenumber() {
		return mobilenumber;
	}

	/**
	 * Sets the mobilenumber.
	 *
	 * @param mobilenumber the mobilenumber to set
	 */
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	/**
	 * Gets the password confirm.
	 *
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * Sets the password confirm.
	 *
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	

}
