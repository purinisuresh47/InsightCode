package com.sms.admintenant.servicesimpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sms.admintenant.dto.SigninDTO;
import com.sms.admintenant.exceptions.CustomException;
import com.sms.admintenant.models.Authority;
import com.sms.admintenant.models.City;
import com.sms.admintenant.models.Email;
import com.sms.admintenant.models.OTP;
import com.sms.admintenant.models.State;
import com.sms.admintenant.models.User;
import com.sms.admintenant.models.UserNotification;
import com.sms.admintenant.repository.CityRepository;
import com.sms.admintenant.repository.EmailRepository;
import com.sms.admintenant.repository.StateRepository;
import com.sms.admintenant.repository.UserNotificationRepository;
import com.sms.admintenant.repository.UserRepository;
import com.sms.admintenant.security.JwtTokenProvider;
import com.sms.admintenant.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	RestTemplate restTemplate;

	RestTemplate restTemplate2;

	@Value("${email}")
	String Email_Id;

	@Value("${password}")
	String password;

	@Autowired
	private UserNotificationRepository userNotificationRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	public String getIp() {
		InetAddress ip;
		String hostname = null;
		try {
			
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostAddress();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostname;

	}

	@Override
	public String signup(User user) throws Exception {

		System.out.println("user::" + user);
		if (user.getRole().equals("ROLE_CLIENT_MASTER")) {
			user.setAuthorities((Authority.ROLE_CLIENT_MASTER).toString());
			user.setRolealias("Client Master");
		} else if (user.getRole().equals("ROLE_SITE_ADMIN")) {
			user.setAuthorities((Authority.ROLE_SITE_ADMIN).toString());
			user.setRolealias("Site Admin");
		} else if (user.getRole().equals("ROLE_SITE_USER")) {
			user.setAuthorities((Authority.ROLE_SITE_USER).toString());
			user.setRolealias("Site User");
		} else if (user.getRole().equals("ROLE_USER")) {
			user.setAuthorities((Authority.ROLE_USER).toString());
			user.setRolealias("User");
		}
		if (!userRepository.existsByUsername(user.getUsername())) {

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setConfirmationToken(UUID.randomUUID().toString());
			Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.port", "587");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true"); // TLS

			Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Email_Id, password);
				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(Email_Id, "AI3O"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
				message.setSubject("AI3O-Registration!");
				message.setText(
						"Hi " + user.getFirstname().substring(0, 1).toUpperCase() + user.getFirstname().substring(1)
								+ "," + "\n" + "\n" + "To confirm your account, please click here : "
								+ "http://"+getIp()+":2137/admintask/users/confirm?token="
								+ user.getConfirmationToken() + "\n" + "\n" + "Thank You," + "\n" + "Team AI3O.");
				System.out.println("********************* Before Send Email *********************");
				Transport.send(message);
				System.out.println("********************* Email sent successfully *********************");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			userRepository.save(user);

			// emailSenderService.sendEmail(mailMessage);

			return jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities());

		} else {
			throw new CustomException("User name is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public String confirmUserAccount(String confirmationToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		headers.setAccessControlAllowOrigin("*");
		headers.setOrigin("*");
		List<HttpMethod> al = new ArrayList<HttpMethod>();
		al.add(HttpMethod.DELETE);
		al.add(HttpMethod.PUT);
		al.add(HttpMethod.POST);
		al.add(HttpMethod.GET);
		headers.setAccessControlAllowMethods(al);

		if (Objects.nonNull(userRepository.findByConfirmationToken(confirmationToken))) {
			User user = userRepository.findByConfirmationToken(confirmationToken);
			user.setVerified(true);
			userRepository.save(user);
			return "Your Email Verified Successfully : " + user.getEmail();
		} else {
			return null;
		}
	}

	@Override
	public SigninDTO signin(String username, String password) {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new CustomException("Invalid credentials", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!user.isVerified() && !("ROLE_ADMIN".equals(user.getRole()))) {
			throw new CustomException("Email verification is required to login", HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (username.equals(user.getUsername()) && password.equals(user.getPasswordConfirm())) {
			String email = user.getEmail();
			// List<Authority> authorities = user.getAuthorities();
			String authorities = user.getAuthorities();

			String token = jwtTokenProvider.createToken(username,
					userRepository.findByUsername(username).getAuthorities());

			SigninDTO dto = new SigninDTO();
			dto.setAuthorities(authorities);
			dto.setToken(token);
			dto.setEmail(email);
			dto.setOrganizationName(user.getOrganizationName());

			return dto;
		} else {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public OTP forgotPassword(String userName) throws Exception {

		User user = userRepository.findByUsername(userName);
		String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
		if (user != null) {
			OTP otp1 = new OTP();
			try {

				String mail_subject = "Forgot Password";

				Properties props = System.getProperties();
				String host_name = "smtp.gmail.com";
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", host_name);
				props.put("mail.smtp.user", Email_Id);
				props.put("mail.smtp.password", password);
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.auth", "true");

				Session session = Session.getDefaultInstance(props);

				MimeMessage message = new MimeMessage(session);

				try {
					message.setFrom(new InternetAddress(Email_Id, "AI3O"));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
					message.setSubject(mail_subject);
					message.setHeader("content-Type", "text/html;charset=\"ISO-8859-1\"");
					MimeMultipart multipart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();

					Map<String, String> input = new HashMap<String, String>();
					input.put("Author", "spsoftglobal.com");
					input.put("Topic", "HTML Template for Email");
					input.put("Content In", "English");
					messageBodyPart.setText("Hi " + user.getFirstname().substring(0, 1).toUpperCase()
							+ user.getFirstname().substring(1) + "," + "\n" + "\n" + "Your verification code is: " + otp
							+ "\n" + "This OTP is valid for only 5 minutes." + "\n" + "\n"
							+ "If you are having any issues with your account, please don't hesitate to contact us."
							+ "\n" + "\n" + "Thank You," + "\n" + "Team AI3O.");
					multipart.addBodyPart(messageBodyPart);
					otp1.setEmail(user.getEmail());
					otp1.setOTP(otp);
					message.setContent(multipart);

					Transport transport = session.getTransport("smtp");
					transport.connect(host_name, Email_Id, password);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();

				} catch (MessagingException ex) {
					ex.printStackTrace();
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return otp1;
		} else {
			throw new Exception("User name doesn't exist");
		}

	}

	@Override
	public User findByUsername(String userName) {
		return userRepository.findByUsername(userName);
	}

	@Override
	public User findByOrganizationName(String orgName) {
		return userRepository.findByOrganizationName(orgName);
	}

	public User savePassword(User user) {
		return userRepository.save(user);
	}

	@Override
	public String generateToken(User user) {
		return jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities());
	}

	@Override
	public User findById(Long userId) {
		Optional<?> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent())
			return (User) optionalUser.get();
		else
			return null;
	}

	@Override
	public void emailUpdate(User user) throws Exception {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		/*
		 * ConfirmationToken confirmationToken = new ConfirmationToken(user);
		 * confirmationTokenRepository.save(confirmationToken);
		 */
		user.setConfirmationToken(UUID.randomUUID().toString());
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Email_Id, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Email_Id, "AI3O"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
			message.setSubject("Email Confirmation!");
			message.setText("Hi " + user.getFirstname().substring(0, 1).toUpperCase() + user.getFirstname().substring(1)
					+ "," + "\n" + "\n" + "To verify your email, please click here : "
					+ "http://"+getIp()+":2137/admintask/users/confirm?token=" + user.getConfirmationToken() + "\n"
					+ "\n" + "Thank You," + "\n" + "Team AI3O.");
			System.out.println("********************* Before Send Email *********************");
			Transport.send(message);
			System.out.println("********************* Email sent successfully *********************");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateMail(String oldMail, String newMail) {
		List<UserNotification> un = userNotificationRepository.findByEmail(oldMail);
		for (UserNotification uns : un) {
			uns.setEmail(newMail);
			userNotificationRepository.save(uns);
			System.out.println("updated success");
		}
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);

	}

	@Override
	public void saveNotification(UserNotification userNotification) {

		userNotificationRepository.save(userNotification);
	}

	@Override
	public void saveEmail(Email email) {
		emailRepository.save(email);

	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User deleteUser(User user) {
		userRepository.delete(user);
		return user;
	}

	@Override
	public List<User> findByRole(String role) {
		List<User> listusers = new ArrayList<User>();
		List<User> dbUsers = userRepository.findAll();
		if (!dbUsers.isEmpty()) {
			for (User users : dbUsers) {
				if (users.getRole().equals(role)) {
					listusers.add(users);
					Collections.reverse(listusers);
				}
			}

			return listusers;
		}
		return null;
	}

	@Override
	public List<State> getStateByCountryId(String countryId) {
		List<State> listofStates = new ArrayList<State>();
		List<State> dbstates = stateRepository.findAllByCid(countryId);
		if (!dbstates.isEmpty()) {
			for (State states : dbstates) {
				if (states.getCid().equals(countryId)) {
					listofStates.add(states);
				}
			}
			return listofStates;
		}
		return null;
	}

	@Override
	public List<City> getCityByStateId(String stateId) {
		List<City> listofcities = new ArrayList<City>();
		// List<City> dbcities = cityRepository.findAll();
		List<City> dbcities = cityRepository.findAllBySid(stateId);
		if (!dbcities.isEmpty()) {
			for (City cities : dbcities) {
				if (cities.getSid().equals(stateId)) {
					listofcities.add(cities);
				}
			}
			return listofcities;
		}
		return null;
	}

	@Override
	public List<User> getSiteAdmins(String clientMaster) {

		List<User> dbusers = userRepository.findAllByClientmaster(clientMaster);
		List<User> siteadmins = new ArrayList<>();
		if (!dbusers.isEmpty()) {
			for (User users : dbusers) {
				String siteadmin = users.getSiteadmin();
				if (Objects.isNull(siteadmin)) {
					siteadmins.add(users);
				}
			}
			return siteadmins;
		}
		return null;
	}

	@Override
	public List<User> getSiteUsers(String clientMaster, String siteAdmin) {
		List<User> dbsiteusers = userRepository.findAllBySiteadmin(siteAdmin);
		List<User> siteusers = new ArrayList<>();
		if (!dbsiteusers.isEmpty()) {
			for (User users : dbsiteusers) {
				if (users.getClientmaster().equals(clientMaster)) {
					siteusers.add(users);
				}
			}
			return siteusers;
		}
		return null;
	}

}
