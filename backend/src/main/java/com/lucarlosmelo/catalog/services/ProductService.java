package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    Page<ProductResponse> findAllPaged(Pageable pageable);

    ProductResponse findById(UUID id);

    ProductResponse insert(ProductInsertRequest request);

    ProductResponse update(UUID id, ProductUpdateRequest request);

    void delete(UUID id);

}
