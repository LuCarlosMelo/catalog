package com.lucarlosmelo.catalog.dtos.categories;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucarlosmelo.catalog.entities.Category;

@JsonSerialize
public class CategoryResponse {
    private Long id;
    private String name;
    public CategoryResponse(Category category){
        id = category.getId();;
        name = category.getName();
    }
    public CategoryResponse(CategoryRequest categoryRequest){
        id = categoryRequest.getId();
        name = categoryRequest.getName();
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
