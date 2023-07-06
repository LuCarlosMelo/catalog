package com.lucarlosmelo.catalog.resources;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import com.lucarlosmelo.catalog.resources.exceptions.StandardError;
import com.lucarlosmelo.catalog.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/products", produces = {"application/json"})
@SecurityScheme(
		name = "Authentication",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer"
)
@Tag(name = "Product")
public class ProductResource {

	private final ProductService service;

	public ProductResource(ProductService service) {
		this.service = service;
	}

	@GetMapping()
	@Operation(summary = "Get all products paginated", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<Page<ProductResponse>> findAll(@ParameterObject Pageable pageable) {
		var products = service.findAllPaged(pageable);
		return ResponseEntity.ok(products);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Get product by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class))),})
	public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
		var product = service.findById(id);
		return ResponseEntity.ok(product);
	}

	@PostMapping()
	@Operation(summary = "Insert product by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<ProductResponse> insert(@Valid @RequestBody() ProductInsertRequest request){
		var product = service.insert(request);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(product);
	}

	@PutMapping(value = "/{id}")
	@Operation(summary = "Update product by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @RequestBody ProductUpdateRequest request){
		var product = service.update(id, request);
		return ResponseEntity.ok(product);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete product by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
	public ResponseEntity<Void> delete(@PathVariable UUID id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
