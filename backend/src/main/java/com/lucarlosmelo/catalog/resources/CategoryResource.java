package com.lucarlosmelo.catalog.resources;

import com.lucarlosmelo.catalog.dtos.categories.CategoryResponse;
import com.lucarlosmelo.catalog.resources.exceptions.StandardError;
import com.lucarlosmelo.catalog.services.CategoryService;
import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories paginated")
    @ApiResponse(responseCode = "200", description = "Sucessful")
    public ResponseEntity<Page<CategoryResponse>> findAll(@ParameterObject Pageable pageable) {
        var categories = categoryService.findAllPaged(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/{id}")
	@Operation(summary = "Get category by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucessful"),
			@ApiResponse(responseCode = "404", description = "Not Found",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        var category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
	@Operation(summary = "Insert category by ID",
			security = @SecurityRequirement(name = "Authentication"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sucessful"),
			@ApiResponse(responseCode = "400", description = "Bad Request",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity",
					content = @Content(schema = @Schema(implementation = StandardError.class)))})
    public ResponseEntity<CategoryResponse> insert(@RequestBody CategoryRequest categoryRequest) {
        var category = categoryService.insert(categoryRequest);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping(value = "/{id}")
	@Operation(summary = "Update category by ID",
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
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
       	var category = categoryService.update(id, categoryRequest);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete category by ID",
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
