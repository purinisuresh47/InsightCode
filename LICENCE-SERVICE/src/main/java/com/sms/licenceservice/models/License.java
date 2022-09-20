package com.sms.licenceservice.models;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siva
 *
 */
@Entity
@Table(name = "LICENSE")
public class License {

	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private String username;
	
	@Column
	private long productId;

	@Column
	private String startDate;

	@Column
	private String paymentMode;

	@Column
	private String transactionNumber;

	@Column
	private boolean trial;

	@Column
	private String endDate;

	@Column
	private int extensionDays;

	@Column
	private String assignedBy;

	@Column
	private String subscribedBy;
	
	@Column
	private String productDuration;

	@Column
	private String trialDuration;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public boolean isTrial() {
		return trial;
	}

	public void setTrial(boolean trial) {
		this.trial = trial;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getExtensionDays() {
		return extensionDays;
	}

	public void setExtensionDays(int extensionDays) {
		this.extensionDays = extensionDays;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getSubscribedBy() {
		return subscribedBy;
	}

	public void setSubscribedBy(String subscribedBy) {
		this.subscribedBy = subscribedBy;
	}

	public String getProductDuration() {
		return productDuration;
	}

	public void setProductDuration(String productDuration) {
		this.productDuration = productDuration;
	}

	public String getTrialDuration() {
		return trialDuration;
	}

	public void setTrialDuration(String trialDuration) {
		this.trialDuration = trialDuration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
