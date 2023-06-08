package com.lucarlosmelo.catalog.services.utils;

import com.lucarlosmelo.catalog.dtos.CategoryRequest;
import com.lucarlosmelo.catalog.dtos.users.UserInsertRequest;
import com.lucarlosmelo.catalog.dtos.users.UserUpdateRequest;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.repositories.CategoryRepository;
import com.lucarlosmelo.catalog.repositories.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Util {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    public Util(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    public void copyProperties(UserInsertRequest userInsertRequest, User user) {
        BeanUtils.copyProperties(userInsertRequest, user);
        user.getRoles().clear();
        userInsertRequest.getRoles().forEach(role -> user.getRoles().add(roleRepository.getOne(role.getId())));
    }

    public void copyProperties(UserUpdateRequest userUpdateRequest, User user) {
        updatesValid(userUpdateRequest, user);
        user.getRoles().clear();
        userUpdateRequest.getRoles().forEach(role -> user.getRoles().add(roleRepository.getOne(role.getId())));
    }

    public CategoryRequest copyProperties(CategoryRequest request, Category category){
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return new CategoryRequest(category);
    }

    private void updatesValid(UserUpdateRequest request, User user) {
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
    }

}
