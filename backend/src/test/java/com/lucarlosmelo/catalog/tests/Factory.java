package com.lucarlosmelo.catalog.tests;

import java.time.Instant;

import com.lucarlosmelo.catalog.dtos.ProductDTO;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product p = new Product(1L, "Phone", 800d, Instant.parse("2020-10-20T03:00:00Z"), "Good Phone", "https:/img.com/img.png");
		p.getCategories().add(createCategory());
		return p;
		
	}
	
	public static ProductDTO createProductDTO() {
		Product p = createProduct();
		return new ProductDTO(p, p.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(2L, "Eletronics");
	}
	
}
