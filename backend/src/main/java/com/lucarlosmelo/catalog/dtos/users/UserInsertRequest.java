package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.Role;
import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.services.validation.UserInsertValid;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@UserInsertValid
public class UserInsertRequest implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Size(min = 3, max = 30)
    @NotBlank(message = "Campo obrigatório")
    @Schema(description = "User first name", example = "Hamilton")
    private String firstName;
    @Size(min = 3, max = 30)
    @NotBlank(message = "Campo obrigatório")
    @Schema(description = "User last name", example = "Brown")
    private String lastName;
    @Email
    @Schema(description = "User email", example = "hamilton@email.com")
    private String email;

    @Schema(description = "Access password", example = "********")
    private String password;

    @Schema(description = "User roles", example = "[{\"id\" : 1, \"id\" : 2}]")
    private Set<RoleRequest> roles = new HashSet<>();

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
