package com.hainam.judgeql.category.mapper;

import com.hainam.judgeql.category.domain.Category;
import com.hainam.judgeql.category.dto.request.CreateCategoryRequest;
import com.hainam.judgeql.category.dto.response.CategoryResponse;

public class CategoryMapper {
    public static Category toEntity(CreateCategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(category.getCreatedBy())
                .updatedBy(category.getUpdatedBy())
                .build();
    }

    public static void updateEntity(Category category, CreateCategoryRequest request) {
        category.setName(request.getName());
    }
}
