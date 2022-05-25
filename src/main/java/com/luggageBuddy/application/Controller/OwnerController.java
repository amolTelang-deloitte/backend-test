package com.luggageBuddy.application.Controller;

import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Entity.Orders;
import com.luggageBuddy.application.Repository.OrderRepository;
import com.luggageBuddy.application.Service.LocationService;
import com.luggageBuddy.application.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OwnerController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;

    //get location by location phno
    @GetMapping("/location/api/get/{id}")
    public Location getLocationById(@PathVariable String id){
        return locationService.getLocationByPhno(id);
    }


    //update location info
    @PutMapping("/location/api/put/{id}")
    public ResponseEntity updateLocationInfo(@RequestBody Location loc, @PathVariable String id){
        return locationService.updateLocationInfo(loc,id);
    }

    //update location availability by Id
    @PutMapping("/location/api/put/availability/{id}/{state}")
    public ResponseEntity updateAvailability(@PathVariable String id,@PathVariable Boolean state){
        return locationService.updateAvailability(id,state);
    }


    //get All orders
    @GetMapping("/order/api/get/all")
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    //get all order by location id
    @GetMapping("/order/api/get/location/{id}")
    public List<Orders> getAllOrderByLocationId(@PathVariable String id){
        return orderService.getAllOrdersByLocationId(id);
    }

    //get all orders by customer id
    @GetMapping("/order/api/get/customer/{id}")
    public List<Orders> getAllOrdersByCustomerId(@PathVariable String id){
        return orderService.getAllOrdersByCustomerId(id);
    }
    //get order by order Id
    @GetMapping("/order/api/get/order/{id}")
    public Orders getOrderByOrderId(@PathVariable Long id){
        return orderService.getOrderByOrderId(id);
    }

    //get final amount
    @GetMapping("/order/api/get/finalAmount/{id}")
    public float getFinalAmount(@PathVariable Long id){
        return orderService.getFinalPrice(id);
    }

    //get pending orders
    @GetMapping("/order/api/get/pendingOrders/{id}")
    public List<Orders> getPendingOrders(@PathVariable String id){
        return orderService.getPendingOrdersByLocationId(id);
    }


    //add an order
    @PostMapping("order/api/post/add/{cityName}/{locId}/{customerId}")
    public ResponseEntity addOrder(@RequestBody Orders order, @PathVariable String locId, @PathVariable String customerId, @PathVariable String cityName){

        return orderService.addOrder(order,locId,customerId,cityName);
    }

    //complete order
    @PostMapping("/order/api/payment/{orderId}/{paidAmount}")
    public ResponseEntity completeOrder(@PathVariable Long orderId,@PathVariable float paidAmount){
        return orderService.completeOrder(orderId,paidAmount);
    }

}
