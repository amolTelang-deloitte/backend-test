package com.luggageBuddy.application.Controller;

import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Entity.Role;
import com.luggageBuddy.application.Entity.User;
import com.luggageBuddy.application.Repository.RoleRepository;
import com.luggageBuddy.application.Service.LocationService;
import com.luggageBuddy.application.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UtilController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final LocationService locationService;


    //get all users
    @GetMapping("/admin/api/getAllUsers")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/util/api/get/user/{id}")
    public User getUserById(@PathVariable String id){
        User tempUser=new User();
        tempUser=userService.getUser(id);
        return tempUser;
    }
    //register a user
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user){
            User tempUser=new User(null, user.getPhoneNo(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),new ArrayList<>());
        Role role=roleRepository.findByName("CUSTOMER");
        tempUser.getRoles().add(role);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/register").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(tempUser));
    }

    //get locations by cityname
    @GetMapping("/util/api/get/city/{cityname}")
    public List<Location> getLocationByCityname(@PathVariable String cityname){
        return locationService.getLocationsByCityname(cityname);
    }



}





// util functions may be used later----------------------------------------------

//adds role to the database
//    @PostMapping("/addRole")
//    public ResponseEntity<Role>saveRole(@RequestBody Role role){
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/addRole").toUriString());
//        return ResponseEntity.created(uri).body(userService.saveRole(role));
//    }

//adds role to the user
//adds role to the user
//    @PostMapping ("/addRoleUser")
//    public ResponseEntity addRoleToUser(@RequestBody RoleToUser form){
//        userService.addRoleToUser(form.getUsername(), form.getRoleName());
//        return new ResponseEntity("role added to user", HttpStatus.CREATED);
//    }
