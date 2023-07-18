package com.lucarlosmelo.catalog.resources;

import com.lucarlosmelo.catalog.dtos.users.UserInsertRequest;
import com.lucarlosmelo.catalog.dtos.users.UserResponse;
import com.lucarlosmelo.catalog.dtos.users.UserUpdateRequest;
import com.lucarlosmelo.catalog.resources.exceptions.StandardError;
import com.lucarlosmelo.catalog.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	private final UserService service;

	public UserResource(UserService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Get all users paginated",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponse(responseCode = "200", description = "Sucessful")
	public ResponseEntity<Page<UserResponse>> findAll(@ParameterObject Pageable pageable) {
		Page<UserResponse> users = service.findAllPaged(pageable);
		return ResponseEntity.ok(users);
	}  
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Get user by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
		var user = service.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
	@Operation(summary = "Insert user by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<String> insert(@Valid @RequestBody UserInsertRequest userInsertRequest){
		var user = service.insert(userInsertRequest);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body("Criado com sucesso. Seu username: " + user.getEmail());
	}

	@PutMapping(value = "/{id}")
	@Operation(summary = "Update user by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<UserResponse> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
		var user = service.update(id, userUpdateRequest);
		return ResponseEntity.ok(user);
	}
	 
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete user by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<Void> delete(@PathVariable UUID id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
