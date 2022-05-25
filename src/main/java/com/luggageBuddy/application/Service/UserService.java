package com.luggageBuddy.application.Service;

import com.luggageBuddy.application.Entity.Role;
import com.luggageBuddy.application.Entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    User getUser(String username);
    List<User> getUsers();
}
