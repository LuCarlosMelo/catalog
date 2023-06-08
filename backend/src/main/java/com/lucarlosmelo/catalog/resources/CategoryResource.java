package com.lucarlosmelo.catalog.resources;

import com.lucarlosmelo.catalog.dtos.CategoryResponse;
import com.lucarlosmelo.catalog.services.ImplCategoryService;
import com.lucarlosmelo.catalog.dtos.CategoryRequest;
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
	private ImplCategoryService categoryService;

	@GetMapping
	public ResponseEntity<Page<CategoryResponse>> findAll(Pageable pageable ) {
		var categories = categoryService.findAllPaged(pageable);
		return ResponseEntity.ok(categories);
	}  
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
		var category = categoryService.findById(id);
		return ResponseEntity.ok(category);
	}
	
	@PostMapping 
	public ResponseEntity<CategoryRequest> insert(@RequestBody CategoryRequest categoryRequest){
		var category = categoryService.insert(categoryRequest);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(category.getId()).toUri();
		return ResponseEntity.created(uri).body(category);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryRequest> update(@PathVariable Long id, @RequestBody CategoryRequest category){
		category = categoryService.update(id, category);
		return ResponseEntity.ok(category);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
