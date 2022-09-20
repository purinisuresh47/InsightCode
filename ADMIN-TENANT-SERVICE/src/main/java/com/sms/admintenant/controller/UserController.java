package com.sms.admintenant.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.sms.admintenant.dto.SigninDTO;
import com.sms.admintenant.dto.TokenDTO;
import com.sms.admintenant.exceptions.ExceptionResponse;
import com.sms.admintenant.models.City;
import com.sms.admintenant.models.Country;
import com.sms.admintenant.models.Email;
import com.sms.admintenant.models.OTP;
import com.sms.admintenant.models.State;
import com.sms.admintenant.models.Tenant;
import com.sms.admintenant.models.User;
import com.sms.admintenant.models.UserNotification;
import com.sms.admintenant.repository.CityRepository;
import com.sms.admintenant.repository.CountryRepository;
import com.sms.admintenant.repository.StateRepository;
import com.sms.admintenant.repository.UserRepository;
import com.sms.admintenant.services.IUserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ModelMapper modelMapper;

	
	@Value("${email}")
	String Email_Id;

	@Value("${ClientMailId}")
	String ClientMailId;

	@Value("${password}")
	String password;

	String licenseUri = "http://localhost:2175/paaslicense/license/deactivate/";
	String licenceUri = "http://localhost:2175/paaslicense/license/";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@PostMapping("/signup")
	public String signup(@RequestBody User user) throws Exception {
		return userService.signup(modelMapper.map(user, User.class));
	}

	@RequestMapping(value = "/confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmAccount(@RequestParam("token") String confirmationToken) {
		String msg = userService.confirmUserAccount(confirmationToken);
		if (msg != null) {
			return verificationSuccess();
		} else {
			return verificationFailed();
		}
	}

	public ModelAndView verificationSuccess() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accountVerified");
		return modelAndView;
	}

	public ModelAndView verificationFailed() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("invalidLink");
		return modelAndView;
	}

	@PostMapping("/signin")
	public SigninDTO login(@RequestBody SigninDTO dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();
		return userService.signin(username, password);
	}

	@PostMapping("/forgotPassword/{userName}")
	public OTP forgotPassword(@PathVariable String userName) throws Exception {
		OTP otp = userService.forgotPassword(userName);
		return otp;
	}

	@PutMapping("/resetPassword/{userName}")
	public User resetPassword(@RequestBody Tenant tenant, @PathVariable String userName) {
		User user2 = userService.findByUsername(userName);
		user2.setPassword(tenant.getNewPassword());
		user2.setPasswordConfirm(tenant.getNewPassword());
		return userService.savePassword(user2);
	}

	@PutMapping("/changePassword/{userName}")
	public User changePassword(@RequestBody Tenant tenant, @PathVariable String userName) throws Exception {
		User user2 = userService.findByUsername(userName);
		String oldpwd = user2.getPasswordConfirm();
		if (oldpwd.equals(tenant.getOldPassword())) {
			user2.setPassword(tenant.getNewPassword());
			user2.setPasswordConfirm(tenant.getNewPassword());
			return userService.savePassword(user2);
		} else {
			throw new Exception("Invalid old password");
		}

	}

	@GetMapping("/getUser/{userName}")
	public User getUser(@PathVariable String userName) {
		User user = userService.findByUsername(userName);
		if (user != null) {
			return user;
		}
		return null;
	}

	@GetMapping("/generateToken/{userName}")
	public ResponseEntity<TokenDTO> generateToken(@PathVariable String userName) throws Exception {
		User user = userService.findByUsername(userName);
		if (user == null) {
			return new ResponseEntity<TokenDTO>(HttpStatus.FORBIDDEN);
		}
		String token = userService.generateToken(user);

		TokenDTO dto = new TokenDTO();
		dto.setTokenName(token);
		return new ResponseEntity<TokenDTO>(dto, HttpStatus.CREATED);
	}

	@PutMapping("/user/update")
	public ResponseEntity<User> updateUser(@RequestBody User userDetails, HttpServletRequest request) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		System.out.println(" ********* Token *********** ::" + token);
		headers.set("Authorization", token);
		// HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		User user = userService.findById(userDetails.getUserId());

		if (userDetails.getMobilenumber() != null) {
			user.setMobilenumber(userDetails.getMobilenumber());
		}
		if (userDetails.getPassword() != null) {
			user.setPassword(userDetails.getPassword());
		}
		if (userDetails.getPasswordConfirm() != null) {
			user.setPasswordConfirm(userDetails.getPasswordConfirm());
		}
		if (userDetails.getExtensionNumber() != null) {
			user.setExtensionNumber(userDetails.getExtensionNumber());
		}
		if (userDetails.getCountry() != null) {
			user.setCountry(userDetails.getCountry());
		}
		if (userDetails.getState() != null) {
			user.setState(userDetails.getState());
		}
		if (userDetails.getCity() != null) {
			user.setCity(userDetails.getCity());
		}
		if (userDetails.getLocation() != null) {
			user.setLocation(userDetails.getLocation());
		}
		if (userDetails.getOrganizationName() != null) {
			user.setOrganizationName(userDetails.getOrganizationName());
		}
		if (userDetails.getUserImage() != null) {
			user.setUserImage(userDetails.getUserImage());
		}
		if (userDetails.getSiteadmin() != null) {
			user.setSiteadmin(userDetails.getSiteadmin());
		}
		if (userDetails.getClientmaster() != null) {
			user.setClientmaster(userDetails.getClientmaster());
		}
		if (userDetails.getFirstname() != null) {
			user.setFirstname(userDetails.getFirstname());
		}
		if (userDetails.getLastname() != null) {
			user.setLastname(userDetails.getLastname());
		}
		if (!(user.getEmail().equalsIgnoreCase(userDetails.getEmail()))) {
			user.setVerified(false);
			userService.emailUpdate(userDetails);
			userService.updateMail(user.getEmail(), userDetails.getEmail());
		}
		if (userDetails.getEmail() != null) {

			user.setEmail(userDetails.getEmail());
		}

		User updatedTenant = userService.save(user);

		return ResponseEntity.ok(updatedTenant);

	}

	@RequestMapping(value = "/sendMail", method = { RequestMethod.POST })
	public void sendMail(@RequestBody Email email) throws MessagingException {
		try {

			Properties props = System.getProperties();
			String host_name = "smtp.gmail.com";
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host_name);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Email_Id, password);
				}
			});

			Message message = new MimeMessage(session);

			try {
				message.setFrom(new InternetAddress(Email_Id, "AI3O"));

				String[] to = email.getEmails();
				InternetAddress[] toAddress = new InternetAddress[to.length];

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email_Id));
				for (int i = 0; i < to.length; i++) {
					toAddress[i] = new InternetAddress(to[i]);
					System.out.println("To address  ::" + toAddress[i]);
				}

				for (int i = 0; i < toAddress.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, toAddress[i]);
				}
				message.setSubject(email.getSubject());
				message.setText(email.getMessage());
				Transport.send(message);
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
				final LocalDateTime now = LocalDateTime.now();
				for (int i = 0; i < to.length; i++) {

					UserNotification userNotification = new UserNotification();
					userNotification.setEmail(to[i]);
					userNotification.setEmailFrom(email.getEmailFrom());
					userNotification.setSubject(email.getSubject());
					userNotification.setMessage(email.getMessage());
					userNotification.setDate(dtf.format(now));
					userNotification.setTime(dtfTime.format(now));
					userService.saveNotification(userNotification);

				}
				email.setDate(dtf.format(now));
				email.setTime(dtfTime.format(now));
				userService.saveEmail(email);
				System.out.println("Mail sent successfully");
			} catch (MessagingException ex) {
				ex.printStackTrace();
			} catch (Exception ae) {
				ae.printStackTrace();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@GetMapping("/findAllUsers")
	public List<User> findAllUsers() {
		if (userService.findAllUsers() != null) {
			List<User> userList = userService.findAllUsers();
			List<User> users = userList.stream().filter(user -> !user.getRole().equalsIgnoreCase("ROLE_ADMIN"))
					.collect(Collectors.toList());
			return users;
		}
		return Collections.emptyList();
	}

	@DeleteMapping("/delete/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName, HttpServletRequest request) {
		User user = userService.findByUsername(userName);
		String emailId = user.getEmail();

		userService.deleteUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(licenseUri + userName, HttpMethod.DELETE, entity, String.class);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(new ExceptionResponse(new Date(), "Record deleted", userName));
	}

	@GetMapping("/getTenantCount")
	public long getTenantCount() {
		// return userRepository.countByRole("ROLE_USER");

		return userRepository.count();
	}

	@GetMapping("/getClientMastersCount")
	public long getClientMasterCount() {
		return userRepository.countByRole("ROLE_CLIENT_MASTER");
	}

	@GetMapping("/getClientMasteruserCount/{clientMaster}")
	public long getSiteAdminsAndSiteUsersCount(@PathVariable String clientMaster) {
		return userRepository.countByClientmaster(clientMaster);
	}

	@GetMapping("/getSiteAdminsCount")
	public long getSiteAdminCount() {
		return userRepository.countByRole("ROLE_SITE_ADMIN");
	}

	@GetMapping("/getCountries")
	public List<Country> getAllCountries() {
		return countryRepository.findAll();

	}

	@GetMapping("/getStates/{countryId}")
	public List<State> getStateByCountryId(@PathVariable String countryId) {
		return userService.getStateByCountryId(countryId);

	}

	@GetMapping("/getCities/{stateId}")
	public List<City> getCityByStateId(@PathVariable String stateId) {
		return userService.getCityByStateId(stateId);
	}

	@GetMapping("/getUsersByRole/{role}")
	public List<User> getUsersByRole(@PathVariable String role) {

		return userService.findByRole(role);

	}

	
	
	@GetMapping("/getSiteAdmins/{clientMaster}")
	public List<User> getSiteAdmins(@PathVariable String clientMaster) {

		List<User> users = userService.getSiteAdmins(clientMaster);
		return users;

	}

	@GetMapping("/getSiteUsers/{clientMaster}/{siteAdmin}")
	public List<User> getSiteUsers(@PathVariable String clientMaster, @PathVariable String siteAdmin) {

		List<User> users = userService.getSiteUsers(clientMaster, siteAdmin);
		return users;
	}

	@GetMapping("/isUser/{userName}")
	public void isUserExist(@PathVariable String userName) throws Exception {

		User user = userService.findByUsername(userName);
		if (user != null) {
			throw new Exception("User name is already exist");
		}
	}

	@GetMapping("/isCompany/{orgName}")
	public void isCompanyExist(@PathVariable String orgName) throws Exception {

		User user = userService.findByOrganizationName(orgName);
		if (user != null) {
			throw new Exception("Company name is already exist");
		}
	}

	@GetMapping("/getAdminUsers/{siteAdmin}")
	public List<User> getSiteAdminUsers(@PathVariable String siteAdmin) {
		String clientMaster = userRepository.findByUsername(siteAdmin).getClientmaster();
		return userService.getSiteUsers(clientMaster, siteAdmin);
	}
}
