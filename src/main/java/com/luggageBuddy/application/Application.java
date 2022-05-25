package com.luggageBuddy.application;

import com.luggageBuddy.application.Entity.Role;
import com.luggageBuddy.application.Entity.User;
import com.luggageBuddy.application.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public  PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run(UserService userService){
		//creates roles admin,user and owner in the database and adds an admin into the database
		return args -> {
			userService.saveRole(new Role(1l,"ADMIN"));
			userService.saveRole(new Role(2l,"CUSTOMER"));
			userService.saveRole(new Role(3l,"OWNER"));

			userService.saveUser(new User(null,"u1",null,null,null,"root",new ArrayList<>()));

			userService.addRoleToUser("u1","ADMIN");
		};
	}
}
