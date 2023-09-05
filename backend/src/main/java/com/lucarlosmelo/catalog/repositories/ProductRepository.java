package com.lucarlosmelo.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucarlosmelo.catalog.entities.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    long count();
}
