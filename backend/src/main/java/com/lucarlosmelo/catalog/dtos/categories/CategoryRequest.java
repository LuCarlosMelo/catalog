package com.lucarlosmelo.catalog.dtos.categories;

import java.io.Serializable;

import com.lucarlosmelo.catalog.entities.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@Schema(description = "Category name", example = "Book", required = true)
	private String name;

	public CategoryRequest(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryRequest(Category category) {
		this.id = category.getId();
		this.name = category.getName();
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

}
