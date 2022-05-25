package com.luggageBuddy.application.Repository;

import com.luggageBuddy.application.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long>{
    //get orders by locationId
    List<Orders> findAllBylocationId(String locationId);

    //get all orders by customerId
    List<Orders> findAllBycustomerId(String phoneNo);

}
