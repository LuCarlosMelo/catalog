package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.services.validation.UserInsertValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@UserInsertValid
public class UserInsertRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 3, max = 30)
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    @Size(min = 3, max = 30)
    @NotBlank(message = "Campo obrigatório")
    private String lastName;
    @Email
    private String email;

    private String password;
    private Set<RoleRequest> roles = new HashSet<>();

    public UserInsertRequest() {
    }

    public UserInsertRequest(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserInsertRequest(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        user.getRoles().forEach(role -> this.roles.add(new RoleRequest()));
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleRequest> getRoles() {
        return roles;
    }
}
