package com.lucarlosmelo.catalog.dtos.products;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import com.lucarlosmelo.catalog.entities.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize
public class ProductInsertRequest {

	private Long id;

	@Size(min = 4, max = 10)
	@NotBlank(message = "Campo requerido")
	private String name;
	@NotBlank(message = "Campo requerido")

	private String description;
	@Positive(message = "Preço deve ser positivo")
	private Double price;
	private String imgUrl;
	@PastOrPresent(message = "Não pode ser data futura")
	private Instant date;
	
	private List<CategoryRequest> categories = new ArrayList<>();
	
	public ProductInsertRequest() {
		
	}

	public ProductInsertRequest(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imgUrl = product.getImgurl();
		this.date = product.getDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryRequest> getCategories() {
		return categories;
	}
}
