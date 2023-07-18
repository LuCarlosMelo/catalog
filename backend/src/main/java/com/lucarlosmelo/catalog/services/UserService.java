package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.users.UserInsertRequest;
import com.lucarlosmelo.catalog.dtos.users.UserResponse;
import com.lucarlosmelo.catalog.dtos.users.UserUpdateRequest;
import com.lucarlosmelo.catalog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface ImplUserService extends UserDetailsService {

    Page<UserResponse> findAllPaged(Pageable pageable);

    UserResponse findById(UUID id);

    User findByEmail(String email);

    UserInsertRequest insert(UserInsertRequest dto);

    UserUpdateRequest update(UUID id, UserUpdateRequest dto);

    void delete(UUID id);

}
