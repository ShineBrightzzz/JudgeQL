package com.hainam.judgeql.test.mapper;

import com.hainam.judgeql.category.domain.Category;
import com.hainam.judgeql.category.mapper.CategoryMapper;
import com.hainam.judgeql.test.domain.Test;
import com.hainam.judgeql.test.dto.request.CreateTestRequest;
import com.hainam.judgeql.test.dto.response.TestResponse;

public class TestMapper {    public static Test toEntity(CreateTestRequest request, Category category) {
        return Test.builder()
                .name(request.getName())
                .category(category)
                .build();
    }

    public static TestResponse toResponse(Test test) {
        return TestResponse.builder()
                .id(test.getId())
                .name(test.getName())
                .category(CategoryMapper.toResponse(test.getCategory()))
                .createdAt(test.getCreatedAt())
                .updatedAt(test.getUpdatedAt())
                .createdBy(test.getCreatedBy())
                .updatedBy(test.getUpdatedBy())
                .build();
    }

    public static void updateEntity(Test test, CreateTestRequest request, Category category) {
        test.setName(request.getName());
        test.setCategory(category);
    }
}
