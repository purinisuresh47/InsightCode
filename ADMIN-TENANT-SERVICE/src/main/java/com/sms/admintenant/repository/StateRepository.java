package com.sms.admintenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.State;

@Repository
public interface StateRepository extends JpaRepository<State, String> {

	List<State> findAllByCid(String countryId);

	/*
	 * @Query(value = "SELECT name FROM State where country_id=:country_id")
	 * List<String> findbycountry_id(String country_id);
	 * 
	 * @Query(value = "SELECT state_id FROM State where name=:name") String
	 * findByname(String name);
	 */

}
