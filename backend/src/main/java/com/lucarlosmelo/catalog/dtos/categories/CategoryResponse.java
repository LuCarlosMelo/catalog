package com.lucarlosmelo.catalog.dtos.categories;

import com.lucarlosmelo.catalog.entities.Category;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class CategoryResponse {
    private Long id;
    private String name;
    public CategoryResponse(Category category){
        id = category.getId();;
        name = category.getName();
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
