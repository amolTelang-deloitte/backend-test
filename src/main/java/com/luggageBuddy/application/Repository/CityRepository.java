package com.luggageBuddy.application.Repository;

import com.luggageBuddy.application.Entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,String> {
}
