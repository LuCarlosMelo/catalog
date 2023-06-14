package com.lucarlosmelo.catalog.dtos.products;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JsonSerialize
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;

    private List<CategoryRequest> categories = new ArrayList<>();

    public ProductResponse(Product product, Set<Category> categories){
        id = product.getId();
        name = product.getName();;
        description = product.getDescription();
        price = product.getPrice();;
        imgUrl = product.getImgurl();;
        date = product.getDate();
        categories.forEach(category -> this.categories.add(new CategoryRequest(category)));
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public List<CategoryRequest> getCategories() {
        return categories;
    }
}
