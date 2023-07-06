package com.lucarlosmelo.catalog.dtos.products;

import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Product name", example = "PC Gamer")
    private String name;

    @Schema(description = "Product description", example = "PC Gamer TX30")
    private String description;

    @Schema(description = "Product price", example = "200.00")
    private Double price;

    @Schema(description = "Product image URL", example = "https://www.imagens.com")
    private String imgUrl;

    @Schema(description = "Product categories", example = "[{\"id\" : 1, \"id\" : 2}]")
    private final List<CategoryRequest> categories = new ArrayList<>();

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

    public List<CategoryRequest> getCategories() {
        return categories;
    }
}
