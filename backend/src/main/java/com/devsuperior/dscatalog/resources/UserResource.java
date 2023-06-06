package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dtos.users.UserDTO;
import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	private final IUserService service;

	public UserResource(IUserService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		Page<UserDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok(list);
	}  
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		var dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping 
	public ResponseEntity<String> insert(@Valid @RequestBody UserInsertRequest dto){
		var user = service.insert(dto);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body("Criado com sucesso. Seu email: " + user.getEmail());
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update( @PathVariable Long id,@Valid @RequestBody UserUpdateRequest userUpdateRequest){
		var user = service.update(id, userUpdateRequest);
		return ResponseEntity.ok(user);
	}
	 
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
