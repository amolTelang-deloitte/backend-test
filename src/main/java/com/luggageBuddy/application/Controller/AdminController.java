package com.luggageBuddy.application.Controller;

import com.luggageBuddy.application.Entity.City;
import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Entity.Role;
import com.luggageBuddy.application.Entity.User;
import com.luggageBuddy.application.Repository.RoleRepository;
import com.luggageBuddy.application.Service.LocationService;
import com.luggageBuddy.application.Service.UserServiceImpl;
import org.aspectj.lang.annotation.DeclareError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    //get all the locations in the database
    @GetMapping("/admin/api/get/location/all")
    public List<Location> getAllLocations(){
        return locationService.getAllLocations();
    }

    //get all cities in the database
    @GetMapping("/admin/api/get/city/all")
    public List<City> getAllCities(){
        return locationService.getAllCities();
    }

    //get city by cityname
    @GetMapping("/admin/api/get/city/{cityName}")
    public City getCityByName(@PathVariable String cityName){
        return locationService.getCityByName(cityName);
    }

    //get location by locationId

    //add city into the database
    @PostMapping("/admin/api/post/city")
    public ResponseEntity addCity(@RequestBody City newCity){
        return locationService.addCity(newCity);
    }

    //add location into the database
    @PostMapping("/admin/api/post/location")
    public ResponseEntity addLocation(@RequestBody Location newLoc){
       User tempUser=new User(null,newLoc.getLocationPhno(), newLoc.getLocationOwnersFirstName(), newLoc.getLocationOwnersLastName(), newLoc.getLocationEmail(), "root",new ArrayList<>());
        Role role=roleRepository.findByName("OWNER");
        tempUser.getRoles().add(role);
        User tempRes=userService.saveUser(tempUser);

        return locationService.addLocation(newLoc);
    }

    //update pricing of the city
    @PutMapping("/admin/api/put/city/{cityName}")
    public ResponseEntity updateCityInfo(@RequestBody City city, @PathVariable String cityName){
        return locationService.updateCityInfo(city,cityName);
    }

    //delete city by cityname
    @DeleteMapping("/admin/api/delete/city/{cityName}")
    public ResponseEntity deleteCity(@PathVariable String cityName){

        long l=Long.parseLong(cityName);
        ResponseEntity res=userService.deleteUser(l);
        return locationService.deleteCity(cityName);
    }

    //delete location by locationId
    @DeleteMapping("/admin/api/delete/location/{locId}")
    public ResponseEntity deleteLocation(@PathVariable String locId){
        return locationService.deleteLocation(locId);
    }


}
