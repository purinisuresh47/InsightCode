package com.sms.admintenant.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * State collection class
 * 
 * @author KSiva
 *
 */
//@Document(collection = "State")
@Entity
@Table(name = "State")
public class State {

	@Id
	private String sid;

	@Column
	private String sname;

	@Column
	@JsonIgnore
	private String cid;

	@Column
	@JsonIgnore
	private String countrycode;

	@Column
	@JsonIgnore
	private String statecode;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getStatecode() {
		return statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

}
