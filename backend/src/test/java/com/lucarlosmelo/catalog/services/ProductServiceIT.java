package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.repositories.ProductRepository;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private UUID existingId;
	private UUID nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() {
		existingId = UUID.fromString("04b0111e-48ac-4188-8fcf-35691987cc99");
		nonExistingId = UUID.randomUUID();
		countTotalProducts = 11L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		 service.delete(existingId);
		 Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		 Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
	}
	 
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10(){
		var page = PageRequest.of(0,10);
		var result = service.findAllPaged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, page.getPageNumber());
		Assertions.assertEquals(10, page.getPageSize());  
	}

	@Test
	public void findAllPagedShouldReturnEmptyWhenPageDoesNotExist(){
		var page = PageRequest.of(50, 10);
		var result = service.findAllPaged(page);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findAllPagedShouldReturnSortedWhenPageDoesNotExist(){
		var page = PageRequest.of(0, 10, Sort.by("name"));
		var result = service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
}
