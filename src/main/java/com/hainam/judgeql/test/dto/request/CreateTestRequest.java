package com.hainam.judgeql.test.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateTestRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Category ID is required")
    private UUID categoryId;
}
