package com.lucarlosmelo.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucarlosmelo.catalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
