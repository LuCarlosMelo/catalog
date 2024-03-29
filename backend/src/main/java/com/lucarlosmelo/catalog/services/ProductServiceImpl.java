package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import com.lucarlosmelo.catalog.repositories.ProductRepository;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import com.lucarlosmelo.catalog.services.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

 	private final ProductRepository productRepository;

	private final Util util;

	public ProductServiceImpl(ProductRepository productRepository , Util util) {
		this.productRepository = productRepository;
		this.util = util;
	}

	@Transactional(readOnly = true)
	public Page<ProductResponse> findAllPaged(Pageable pageable) {
		var products = productRepository.findAll(pageable);
		return products.map(product -> new ProductResponse(product, product.getCategories()));
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(UUID id) {
		var findProduct = productRepository.findById(id);
		var product = findProduct.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		return new ProductResponse(product, product.getCategories());
	}

	@Transactional
	public ProductResponse insert(ProductInsertRequest request) {
		var copiedProperties = util.copyProperties(request);
		var product = productRepository.save(copiedProperties);
		return new ProductResponse(product, product.getCategories());
	}

	@Transactional
	public ProductResponse update(UUID id, ProductUpdateRequest request) {
		try {
			var product = productRepository.getOne(id);
			util.copyProperties(request, product);
			product = productRepository.save(product);
			return new ProductResponse(product, product.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id não encontrado: " + id);
		}
	}

	public void delete(UUID id) {
		try {
			productRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id não encontrado: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("VIolação de integridade");
		}
	}
	

}
