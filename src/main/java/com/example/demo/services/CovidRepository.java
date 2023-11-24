package com.example.demo.services;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LocationStates;

@Repository
public interface CovidRepository extends CrudRepository<LocationStates,Integer> {

	 @Query(value ="select * from location_states  where country_name = ?", nativeQuery = true)
	  public LocationStates findByCountry(String countryName);
	
	  @Query(value = "select * from location_states order by latest_total_deaths desc limit 10", nativeQuery = true)
	  public List<LocationStates> findcountryByLatestTotalDeaths(int count);

}
