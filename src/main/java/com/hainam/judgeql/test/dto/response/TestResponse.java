package com.hainam.judgeql.test.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.hainam.judgeql.category.dto.response.CategoryResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResponse {
    private UUID id;
    private String name;
    private CategoryResponse category;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
