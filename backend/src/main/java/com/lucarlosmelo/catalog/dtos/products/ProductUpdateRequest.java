package com.lucarlosmelo.catalog.dtos.products;

import com.lucarlosmelo.catalog.dtos.categories.CategoryRequest;
import com.lucarlosmelo.catalog.entities.Category;
import com.lucarlosmelo.catalog.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductUpdateRequest {
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    private final List<CategoryRequest> categories = new ArrayList<CategoryRequest>();

    public ProductUpdateRequest(){}

    public ProductUpdateRequest(Product product, Set<Category> categories) {
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgurl();
        categories.forEach(category -> this.categories.add(new CategoryRequest(category)));
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

    public List<CategoryRequest> getCategories() {
        return categories;
    }
}
