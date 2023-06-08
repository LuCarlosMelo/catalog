package com.devsuperior.dscatalog.dtos;

import com.devsuperior.dscatalog.entities.Category;
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
