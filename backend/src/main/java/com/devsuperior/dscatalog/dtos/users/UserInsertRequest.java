package com.devsuperior.dscatalog.dtos.users;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

import java.io.Serial;

@UserInsertValid
public class UserInsertRequest extends UserDTO {
	@Serial
	private static final long serialVersionUID = 1L;

	private String password;

	UserInsertRequest(){
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
