package com.sms.licenceservice.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sms.licenceservice.models.License;
import com.sms.licenceservice.service.LicenceService;

@RestController
@RequestMapping("/license")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LicenceController {

	//String uri = "http://localhost:2170/rest/v1/cart/";

	@Autowired
	private LicenceService licenseService;

	RestTemplate restTemplate;

	String productUri = "http://localhost:2151/product/paasproduct/";

	/**
	 * 
	 * licence activation buy and try
	 */
	@PostMapping("/activateLicense/{productId}/{trail}/{username}/{role}/{clientmaster}/{siteadmin}")
	public License activateSubscribedLicense(@PathVariable long productId,@PathVariable String trail,@PathVariable String username,
			@PathVariable String role, @PathVariable String clientmaster, @PathVariable String siteadmin,
			HttpServletRequest request)// @PathVariable String paymentMode
			throws Exception {

		if (!clientmaster.equalsIgnoreCase("null")) {
			setLicense(productId, clientmaster, "ROLE_CLIENT_MASTER",trail, request);
		}
		if (!siteadmin.equalsIgnoreCase("null")) {
			setLicense(productId, siteadmin, "ROLE_SITE_ADMIN",trail, request);
		}

		License license = setLicense(productId, username, role,trail, request);

		//license.setSubscribedBy(username);
		/*
		 * if (license != null) { deleteProductFromCart(request, productId, username); }
		 */
		return license;
	}

	private License setLicense(long productId, String username, String role, String trail,HttpServletRequest request)
			throws Exception {
		int days = 0;

		License activatateLicense = new License();
		Calendar c = Calendar.getInstance();
		List<License> licenses = licenseService.findAllByUsername(username);
		
		activatateLicense.setStartDate(setStartDate());
		activatateLicense.setUsername(username);
		activatateLicense.setProductId(productId);
		activatateLicense.setSubscribedBy(username);
		if(trail.equals("forTrail")) {
			activatateLicense.setTrial(true);
		}else {
			
			activatateLicense.setTrial(false);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		restTemplate = new RestTemplate();

		if (activatateLicense.isTrial()) {
			String trialDuration = restTemplate
					.exchange(productUri + "trailduration/" + productId, HttpMethod.GET, entity, String.class)
					.getBody();
			days = Integer.parseInt(trialDuration);
		} else {
			String productDuration = restTemplate
					.exchange(productUri + "productduration/" + productId, HttpMethod.GET, entity, String.class)
					.getBody();
			days = Integer.parseInt(productDuration);
		}

		c.add(Calendar.DAY_OF_MONTH, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		activatateLicense.setEndDate(sdf.format(c.getTime()));
		activatateLicense.setPaymentMode("Online");
		String transactionNumber = new DecimalFormat("000000").format(new Random().nextInt(999999));
		activatateLicense.setTransactionNumber(transactionNumber);

		
		if (licenses != null) {

			for (License license : licenses) {
				if (productId == license.getProductId() && !license.isTrial()) {
					if (role.equalsIgnoreCase("ROLE_USER") || role.equalsIgnoreCase("ROLE_CLIENT_MASTER")) {
						throw new Exception("This product already subscribed by " + username);
					}
					throw new Exception("This product already subscribed by you or your superior");
				}
			}

			activatateLicense = setLicenseProducts(activatateLicense);
		} else {
			activatateLicense = new License();
			activatateLicense = setLicenseProducts(activatateLicense);
		}

		return activatateLicense;
	}

	private License setLicenseProducts(License license) {

		license = licenseService.activateLicense(license);
		return license;
	}

	private String setStartDate() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		final LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	/*
	 * private void deleteProductFromCart(HttpServletRequest request, long
	 * productId, String username) {
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); HttpSession
	 * session = request.getSession(); String token = (String)
	 * session.getAttribute("token"); headers.set("Authorization", token);
	 * HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	 * restTemplate = new RestTemplate();
	 * 
	 * restTemplate.exchange(uri + username + "/" + productId, HttpMethod.DELETE,
	 * entity, String.class);
	 * 
	 * }
	 */

	@GetMapping("/findAllProductIds/{username}")
	public List<Long> getAllLicensedProductIds(@PathVariable String username) {
		List<License> license = licenseService.findAllByUsername(username);

		if (!license.isEmpty()) {
			return license.stream().map(License::getProductId).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@GetMapping("/{username}")
	public List<License> getSubscribedProductsByUsername(@PathVariable String username, HttpServletResponse res) {
		List<License> licenses = licenseService.findAllByUsername(username);
		List<License> subscribedLicence = new ArrayList<>();
		if (!licenses.isEmpty()) {
			for (License licence : licenses) {
				if (!licence.isTrial()) {
					subscribedLicence.add(licence);
				}
			}
			return subscribedLicence;
		}
		res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		return null;
	}

	@GetMapping("/unSubscribedProducts/{username}")
	public List<License> getTrailproducts(@PathVariable String username, HttpServletResponse res) {
		List<License> licenses = licenseService.findAllByUsername(username);
		List<License> subscribedLicence = new ArrayList<>();
		if (!licenses.isEmpty()) {
			for (License licence : licenses) {
				if (licence.isTrial()) {
					subscribedLicence.add(licence);
				}
			}
			return subscribedLicence;
		}
		res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		return null;
	}
	
	@SuppressWarnings("null")
	@PostMapping("/assignProduct/{username}/{productId}/{id}")
	public void assignProducts(@PathVariable long productId, @PathVariable long id, @PathVariable String username,
			ServletResponse response) throws Exception {

		HttpServletResponse servletResponse = (HttpServletResponse) response;
		List<License> licenses = licenseService.findAllByUsername(username);
		License license = licenseService.findById(id);
		boolean flag = false;
		if (licenses != null) {
			if (licenses.contains(productId)) {
				servletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
				flag = true;
			}
			if (!flag) {
				
				License license1 = new License();
				license1.setUsername(username);
				license1.setProductId(productId);
				license1.setAssignedBy(license.getUsername());
				license1.setStartDate(license.getStartDate());
				license1.setEndDate(license.getEndDate());
				license1.setExtensionDays(license.getExtensionDays());
				license1.setSubscribedBy(license.getSubscribedBy());
				license1.setTrial(license.isTrial());
				licenseService.activateLicense(license1);
				
			}
		} else {
			license = new License();
			license.setUsername(username);
			licenseService.activateLicense(license);
		}
	}

	@PutMapping("/extendLicense/{username}/{productId}/{endDate}")
	public void extendLicense(@PathVariable String username, @PathVariable long productId,
			@PathVariable String endDate) {
		List<License> licenses = licenseService.findAllByUsername(username);
		for (License license : licenses) {
			if (productId == license.getProductId()) {
				license.setEndDate(endDate);
				licenseService.activateLicense(license);
			}
		}

	}

}
