package com.lucarlosmelo.catalog.services;

import javax.persistence.EntityNotFoundException;

import com.lucarlosmelo.catalog.dtos.categories.CategoryResponse;
import com.lucarlosmelo.catalog.services.utils.Util;
import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Util util;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Util util) {
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
        var findCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryResponse(findCategory);
    }

    @Transactional
    public CategoryResponse insert(CategoryRequest request) {
        var category = new Category();
        return new CategoryResponse(util.copyProperties(request, category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        try {
            var category = categoryRepository.getOne(id);
            return new CategoryResponse(util.copyProperties(request, category));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id not found" + id);
        }

    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id not found" + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }

    }
}
