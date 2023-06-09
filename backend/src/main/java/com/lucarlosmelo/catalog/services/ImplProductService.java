package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImplProductService {

    Page<ProductResponse> findAllPaged(Pageable pageable);

    ProductResponse findById(Long id);

    ProductInsertRequest insert(ProductInsertRequest request);

    ProductUpdateRequest update(Long id, ProductUpdateRequest request);

    void delete(Long id);

}
