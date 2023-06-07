package com.devsuperior.dscatalog.services.utils;

import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Util {

    private final RoleRepository roleRepository;

    public Util(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void copyProperties(UserInsertRequest userInsertRequest, User entity) {
        BeanUtils.copyProperties(userInsertRequest, entity);
        entity.getRoles().clear();
        userInsertRequest.getRoles().forEach(role -> entity.getRoles().add(roleRepository.getOne(role.getId())));
    }

    private void updatesValid(UserUpdateRequest userUpdateRequest, User entity) {
        if (userUpdateRequest.getFirstName() != null && !userUpdateRequest.getFirstName().isBlank()) {
            entity.setFirstName(userUpdateRequest.getFirstName());
        }
        if (userUpdateRequest.getLastName() != null && !userUpdateRequest.getLastName().isBlank()) {
            entity.setLastName(userUpdateRequest.getLastName());
        }
        if (userUpdateRequest.getEmail() != null) {
            entity.setEmail(userUpdateRequest.getEmail());
        }
    }

    public void copyProperties(UserUpdateRequest userUpdateRequest, User user) {
        updatesValid(userUpdateRequest, user);
        user.getRoles().clear();
        userUpdateRequest.getRoles().forEach(role -> user.getRoles().add(roleRepository.getOne(role.getId())));
    }

}
