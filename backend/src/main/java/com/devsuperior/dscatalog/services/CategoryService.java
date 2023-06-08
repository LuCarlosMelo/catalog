package com.devsuperior.dscatalog.services;

import javax.persistence.EntityNotFoundException;

import com.devsuperior.dscatalog.dtos.CategoryResponse;
import com.devsuperior.dscatalog.services.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.CategoryRequest;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService implements ImplCategoryService {

	private final CategoryRepository categoryRepository;
	private final Util util;

	public CategoryService(CategoryRepository categoryRepository, Util util) {
		this.categoryRepository = categoryRepository;
		this.util = util;
	}

	@Transactional(readOnly = true)
	public Page<CategoryResponse> findAllPaged(Pageable pageable) {
		var categories = categoryRepository.findAll(pageable);
		return categories.map(CategoryResponse::new);
	}

	@Transactional(readOnly = true)
	public CategoryResponse findById(Long id) {
		var findCategory = categoryRepository.findById(id);
		var category = findCategory.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryResponse(category);
	}

	@Transactional
	public CategoryRequest insert(CategoryRequest request) {
		var category = new Category();
		return util.copyProperties(request, category);
	}

	@Transactional
	public CategoryRequest update(Long id, CategoryRequest request) {
		try {
			var category = categoryRepository.getOne(id);
			return util.copyProperties(request, category);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id);
		}

	}

	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found" + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}

	}
}
