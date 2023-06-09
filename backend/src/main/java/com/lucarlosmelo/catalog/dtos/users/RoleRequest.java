package com.lucarlosmelo.catalog.dtos.users;

import com.lucarlosmelo.catalog.entities.Role;

import java.io.Serializable;

public class RoleRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String authority;

	public RoleRequest() {
	}

	public RoleRequest(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}


	public RoleRequest(Role role) {
		super();
		id = role.getId();
		authority = role.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
