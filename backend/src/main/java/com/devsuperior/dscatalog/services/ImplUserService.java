package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserResponse;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ImplUserService extends UserDetailsService {

    Page<UserResponse> findAllPaged(Pageable pageable);

    UserResponse findById(Long id);

    User findByEmail(String email);

    UserInsertRequest insert(UserInsertRequest dto);

    UserUpdateRequest update(Long id, UserUpdateRequest dto);

    void delete(Long id);

}
