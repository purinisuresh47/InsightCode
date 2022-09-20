package com.sms.admintenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>{

	List<UserNotification> findByEmail(String oldMail);

}
