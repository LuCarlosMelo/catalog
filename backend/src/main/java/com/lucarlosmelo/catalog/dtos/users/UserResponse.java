package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.User;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Set<RoleRequest> roles = new HashSet<>();

    public UserResponse(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        user.getRoles().forEach(role -> this.roles.add(new RoleRequest()));
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleRequest> getRoles() {
        return roles;
    }
}
