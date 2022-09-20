package com.sms.licenceservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.licenceservice.models.License;
import com.sms.licenceservice.repository.LicenceRepository;

@Service
public class LicenceServiceImpl implements LicenceService{

	@Autowired
	private LicenceRepository licenseRepository;

	@Override
	public License findByUsername(String username) {
		License license = licenseRepository.findByUsername(username);
		if (license != null) {
			return license;
		}
		return null;
	}
	
	@Override
	public License activateLicense(License license) {
		License result = licenseRepository.save(license);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public List<License> findAllByUsername(String username) {
		List<License> licenses = licenseRepository.findAllByUsername(username);
		return licenses;
	}

	@Override
	public License findById(long id) {
		return licenseRepository.findById(id).get();
	}
}
