package ru.gw3nax.kudagoAPI.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoAPI.controller.dto.CategoryRequest;
import ru.gw3nax.kudagoAPI.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoAPI.entity.Category;

@Component
public class CategoryMapper {
    public CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setSlug(category.getSlug());
        return categoryResponse;
    }

    public Category mapToCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setId(categoryRequest.getId());
        category.setName(categoryRequest.getName());
        category.setSlug(categoryRequest.getSlug());
        return category;
    }
}
