package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.users.UserDTO;
import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    Page<UserDTO> findAllPaged(Pageable pageable);

    UserDTO findById(Long id);

    User findByEmail(String email);

    UserDTO insert(UserInsertRequest dto);

    UserDTO update(Long id, UserUpdateRequest dto);

    void delete(Long id);

}
