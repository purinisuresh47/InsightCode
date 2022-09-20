package com.sms.admintenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.admintenant.models.City;
import com.sms.admintenant.models.Country;
import com.sms.admintenant.models.State;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {


}
