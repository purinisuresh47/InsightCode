package com.sms.licenceservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.licenceservice.models.License;

@Repository
@Transactional
public interface LicenceRepository extends JpaRepository<License, Long>{

	License findByUsername(String username);

	List<License> findAllByUsername(String username);

}
