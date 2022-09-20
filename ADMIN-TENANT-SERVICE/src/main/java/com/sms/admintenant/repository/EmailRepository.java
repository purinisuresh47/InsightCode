package com.sms.admintenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long>{

}
