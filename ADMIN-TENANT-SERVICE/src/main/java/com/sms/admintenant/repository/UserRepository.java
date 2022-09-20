package com.sms.admintenant.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByUsername(String username);

	@Transactional
	String deleteByUsername(String username);

	User findByEmail(String emailId);

	void delete(User user);

	User findByConfirmationToken(String confirmationToken);

	long countByRole(String string);

	Optional<User> findByRole(String role);

	List<User> findAllByClientmaster(String clientMaster);

	List<User> findAllBySiteadmin(String siteAdmin);

	boolean existsByOrganizationName(String organizationName);

	User findByOrganizationName(String orgName);

	long countByClientmaster(String clientMaster);




}
