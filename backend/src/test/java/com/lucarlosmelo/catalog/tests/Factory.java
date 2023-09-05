package com.lucarlosmelo.catalog.tests;

import java.time.Instant;
import java.util.UUID;

import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		var product = new Product(UUID.randomUUID(), "Phone", 800d, Instant.parse("2020-10-20T03:00:00Z"), "Good Phone", "https:/img.com/img.png");
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductResponse createProductResponse() {
		var product = createProduct();
		return new ProductResponse(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(2L, "Eletronics");
	}
	
}
