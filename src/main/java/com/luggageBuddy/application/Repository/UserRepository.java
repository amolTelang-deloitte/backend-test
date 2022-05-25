package com.luggageBuddy.application.Repository;

import com.luggageBuddy.application.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByphoneNo(String username);
}
