package com.lucarlosmelo.catalog.repositories;

import com.lucarlosmelo.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	UUID existingId ;
	UUID nonExistingId;
	Long countTotalProducts;

	@BeforeEach
	void setUp() {
		existingId = UUID.fromString("04b0111e-48ac-4188-8fcf-35691987cc99");
		nonExistingId = UUID.randomUUID();
		countTotalProducts = 11L;
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		var product = repository.findById(existingId);
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
			var product = repository.findById(nonExistingId);
			Assertions.assertTrue(product.isEmpty());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIsNull() {
		var product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, repository.count());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		var result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(nonExistingId));
	}


}
