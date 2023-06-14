package com.lucarlosmelo.catalog.repositories;

import com.lucarlosmelo.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{
    User findByEmail(String email);
}
