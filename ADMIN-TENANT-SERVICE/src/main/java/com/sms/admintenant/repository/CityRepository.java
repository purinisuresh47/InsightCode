package com.sms.admintenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

	List<City> findAllBySid(String stateId);


}
