package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.CategoryRequest;
import com.lucarlosmelo.catalog.dtos.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImplCategoryService {

    Page<CategoryResponse> findAllPaged(Pageable pageable);
    CategoryResponse findById(Long id);

    CategoryRequest insert(CategoryRequest request);

    CategoryRequest update(Long id, CategoryRequest request);

    void delete(Long id);
}
