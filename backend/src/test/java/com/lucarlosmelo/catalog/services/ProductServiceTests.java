package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import com.lucarlosmelo.catalog.repositories.CategoryRepository;
import com.lucarlosmelo.catalog.repositories.ProductRepository;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import com.lucarlosmelo.catalog.services.utils.Util;
import com.lucarlosmelo.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private Util util;

	private UUID existingProductId;
	private UUID nonExistingProductId;
	private UUID dependentId;

	private ProductInsertRequest productInsertRequest;

	private ProductUpdateRequest productUpdateRequest;

	public ProductServiceTests() {
	}

	@BeforeEach
	void setUp() {
		existingProductId = UUID.randomUUID();
		nonExistingProductId = UUID.randomUUID();
		Long existingCategoryId = 1L;
		Long nonExistingCategoryId = 5L;
		dependentId = UUID.randomUUID();
		var product = Factory.createProduct();
		var page = new PageImpl<>(List.of(product));
		var category = Factory.createCategory();

		doNothing().when(productRepository).deleteById(nonExistingProductId);
		doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(nonExistingProductId);
		Mockito.doThrow(DataBaseException.class).when(productRepository).deleteById(dependentId);

		when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
		when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

		when(categoryRepository.getOne(existingCategoryId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingCategoryId)).thenThrow(EntityNotFoundException.class);

		when(productRepository.getOne(existingProductId)).thenReturn(product);
		when(productRepository.getOne(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

		when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

	}

	@Test
	public void findAllShouldReturnPage() {
		var pageable = PageRequest.of(0, 10);
		var products = productService.findAllPaged(pageable);
		Assertions.assertNotNull(products);
		verify(productRepository).findAll(pageable);

	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		var product = productService.findById(existingProductId);
		Assertions.assertNotNull(product);
		verify(productRepository).findById(existingProductId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		Assertions.assertNotNull(productService.update(existingProductId, productUpdateRequest));
	}
	
	@Test
	public void updateShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.update(nonExistingProductId, productUpdateRequest));
	}
		
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingProductId));

	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> productService.delete(existingProductId));
		verify(productRepository, times(1)).deleteById(existingProductId);
	}

	@Test
	public void deleteShouldDoThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingProductId));
		verify(productRepository, times(1)).deleteById(nonExistingProductId);
	}

	@Test
	public void deleteShouldDoThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> productService.delete(dependentId));
		verify(productRepository, times(1)).deleteById(dependentId);
	}
}
