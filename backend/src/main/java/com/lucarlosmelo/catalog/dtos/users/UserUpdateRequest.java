package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.services.validation.UserUpdateValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@UserUpdateValid
public class UserUpdateRequest {
	private static final long serialVersionUID = 1L;

	@Size(min = 3, max = 30)
	private String firstName;

	@Size(min = 3, max = 30)
	private String lastName;

	@Email
	private String email;

	private Set<RoleRequest> roles = new HashSet<>();

	public UserUpdateRequest(){}
	public UserUpdateRequest(User user) {
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		user.getRoles().forEach(role -> this.roles.add(new RoleRequest()));
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

	public Set<RoleRequest> getRoles() {
		return roles;
	}

}
