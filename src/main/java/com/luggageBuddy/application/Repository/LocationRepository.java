package com.luggageBuddy.application.Repository;


import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location,String> {
    List<Location> findAllBycity(String cityname);
}
