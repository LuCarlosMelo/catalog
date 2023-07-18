package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.Role;
import com.lucarlosmelo.catalog.entities.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Set<RoleRequest> roles = new HashSet<>();

    public UserResponse(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
    }
    public UserResponse(User user, Set<Role> roles) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        roles.forEach(role -> this.roles.add(new RoleRequest(role)));
    }

    public UUID getId() {
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
