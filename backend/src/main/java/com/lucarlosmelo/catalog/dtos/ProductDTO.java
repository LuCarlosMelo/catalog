package com.lucarlosmelo.catalog.dtos;

import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	
	public ProductDTO() {
		
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgurl();
		this.date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category> categories ) {
		this(entity);
		categories.forEach(x -> this.categories.add(new CategoryRequest(x)));
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