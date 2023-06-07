package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserResponse;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	private final IUserService service;

	public UserResource(IUserService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
		Page<UserResponse> users = service.findAllPaged(pageable);
		return ResponseEntity.ok(users);
	}  
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		var user = service.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping 
	public ResponseEntity<String> insert(@Valid @RequestBody UserInsertRequest userInsertRequest){
		var user = service.insert(userInsertRequest);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body("Criado com sucesso. Seu username: " + user.getEmail());
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserUpdateRequest> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
		var user = service.update(id, userUpdateRequest);
		return ResponseEntity.ok(user);
	}
	 
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
