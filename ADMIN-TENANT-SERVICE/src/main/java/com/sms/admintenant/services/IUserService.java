package com.sms.admintenant.services;

import java.util.List;

import com.sms.admintenant.dto.SigninDTO;
import com.sms.admintenant.models.City;
import com.sms.admintenant.models.Email;
import com.sms.admintenant.models.OTP;
import com.sms.admintenant.models.State;
import com.sms.admintenant.models.User;
import com.sms.admintenant.models.UserNotification;

public interface IUserService {

	public String signup(User admin)throws Exception;

	public String confirmUserAccount(String confirmationToken);

	public SigninDTO signin(String username, String password);

	public OTP forgotPassword(String userName) throws Exception;
	
	public User findByUsername(String userName);
	
	public User savePassword(User user);
	
	public String generateToken(User user);

	public User findById(Long userId);

	public void emailUpdate(User user) throws Exception;
	
	public void updateMail(String oldMail, String newMail);
	
	public User save(User user);
	
	public void saveNotification(UserNotification userNotification);
	
	public void saveEmail(Email email);
	
	public List<User> findAllUsers();
	
	public User deleteUser(User user);

	public List<User> findByRole(String role);

	public List<State> getStateByCountryId(String countryId);

	public List<City> getCityByStateId(String stateId);
	
	public List<User> getSiteAdmins(String clientMaster);

	public List<User> getSiteUsers(String clientMaster, String siteAdmin);

	public User findByOrganizationName(String orgName);


}
