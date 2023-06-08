package com.lucarlosmelo.catalog.repositories;

import com.lucarlosmelo.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
}
