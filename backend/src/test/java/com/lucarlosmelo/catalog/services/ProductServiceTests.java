package com.lucarlosmelo.catalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.lucarlosmelo.catalog.dtos.ProductDTO;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;
import com.lucarlosmelo.catalog.repositories.CategoryRepository;
import com.lucarlosmelo.catalog.repositories.ProductRepository;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import com.lucarlosmelo.catalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		productDto = Factory.createProductDTO();

		doNothing().when(repository).deleteById(nonExistingId);
		doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataBaseException.class).when(repository).deleteById(dependentId);

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		when(categoryRepository.getOne(existingId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(repository.getOne(existingId)).thenReturn(product);
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(repository.save(ArgumentMatchers.any())).thenReturn(product);

	}

	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> dto = service.findAllPaged(pageable);
		Assertions.assertNotNull(dto);
		verify(repository).findAll(pageable);

	}

	@Test
	public void findByIdShoulReturnProductDTOWhenIdExists() {
		ProductDTO obj = service.findById(existingId);
		Assertions.assertNotNull(obj);
		verify(repository).findById(existingId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		Assertions.assertNotNull(service.update(existingId, productDto));
	}
	
	@Test
	public void updateShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDto);
		});
	}
		
	
	@Test
	public void findByIdShoulThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldDoThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		verify(repository, times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldDoThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
		verify(repository, times(1)).deleteById(dependentId);
	}
}
