package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import com.lucarlosmelo.catalog.dtos.categories.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryResponse> findAllPaged(Pageable pageable);
    CategoryResponse findById(Long id);

    CategoryResponse insert(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
