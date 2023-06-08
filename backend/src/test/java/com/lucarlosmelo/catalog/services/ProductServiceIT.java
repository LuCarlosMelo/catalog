package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.ProductDTO;
import com.lucarlosmelo.catalog.repositories.ProductRepository;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 100L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		 service.delete(existingId);
		 Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		 Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			 service.delete(nonExistingId);
		 });	 
	}
	 
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10(){
		PageRequest page = PageRequest.of(0,10);
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, page.getPageNumber());
		Assertions.assertEquals(10, page.getPageSize());  
	}

	@Test
	public void findAllPagedShouldReturnEmptyWhenPageDoesNotExist(){
		PageRequest page = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(page);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findAllPagedShouldReturnSortedWhenPageDoesNotExist(){
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	
}
