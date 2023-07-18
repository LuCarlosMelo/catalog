package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.services.validation.UserUpdateValid;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@UserUpdateValid
public class UserUpdateRequest {
	@Serial
	private static final long serialVersionUID = 1L;

	@Size(min = 3, max = 30)
	@Schema(description = "User first name", example = "Hamilton")
	private String firstName;
	@Size(min = 3, max = 30)
	@Schema(description = "User last name", example = "Brown")
	private String lastName;
	@Email
	@Schema(description = "User email", example = "hamilton@email.com")
	private String email;

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

	public Set<RoleRequest> getRoles() {
		return roles;
	}

}
