package com.lucarlosmelo.catalog.resources;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import com.lucarlosmelo.catalog.services.ImplProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

	private final ImplProductService service;

	public ProductResource(ImplProductService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable) {
		var products = service.findAllPaged(pageable);
		return ResponseEntity.ok(products);
	}  
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
		var product = service.findById(id);
		return ResponseEntity.ok(product);
	}
	
	@PostMapping 
	public ResponseEntity<ProductInsertRequest> insert(@Valid @RequestBody ProductInsertRequest request){
		var product = service.insert(request);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(product);
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductUpdateRequest> update(@PathVariable Long id, @RequestBody ProductUpdateRequest request){
		var product = service.update(id, request);
		return ResponseEntity.ok(product);
	}
	 
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
