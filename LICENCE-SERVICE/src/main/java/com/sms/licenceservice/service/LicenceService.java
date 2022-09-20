package com.sms.licenceservice.service;

import java.util.List;

import com.sms.licenceservice.models.License;

public interface LicenceService {

	License findByUsername(String username);

	License activateLicense(License license);

	List<License> findAllByUsername(String username);

	License findById(long id);


	

}
