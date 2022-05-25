package com.luggageBuddy.application.Repository;

import com.luggageBuddy.application.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
