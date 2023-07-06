package com.lucarlosmelo.catalog.dtos.products;

import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



public class ProductInsertRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Size(min = 4, max = 10)
	@NotBlank(message = "Campo requerido")
	@Schema(description = "Product name", example = "PC Gamer")
	private String name;
	@NotBlank(message = "Campo requerido")
	@Schema(description = "Product description", example = "SSD 50GB, Intel Core I5")
	private String description;

	@Positive(message = "Preço deve ser positivo")
	@Schema(description = "Product price", example = "200.00",required = true)
	private Double price;

	@Schema(description = "Product image URL", example = "www.google.com/imagens")
	private String imgUrl;

	@PastOrPresent(message = "Não pode ser data futura")
	@Schema(description = "Product insertion date", example = "2023-07-05T20:07:51.969Z", required = true)
	private Instant date;

	@Schema(description = "List of the product categorias", example = "[{\"id\": 1}]")
	private List<CategoryRequest> categories = new ArrayList<>();

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
