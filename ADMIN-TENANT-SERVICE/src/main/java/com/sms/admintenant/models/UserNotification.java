package com.sms.admintenant.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserNotification")
public class UserNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long notificationId;
	
	@Column
	private String subject;
	
	@Column
	private String message;
	
	@Column
	private String email;
	
	@Column
	private String emailFrom;
	
	@Column
	private String date;
	
	@Column
	private String time;
	
	@Column
	private boolean read;

	



	@Override
	public String toString() {
		return "UserNotification [notificationId=" + notificationId + ", subject=" + subject + ", message=" + message
				+ ", email=" + email + ", emailFrom=" + emailFrom + ", date=" + date + ", time=" + time + ", read="
				+ read + "]";
	}

	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

}
