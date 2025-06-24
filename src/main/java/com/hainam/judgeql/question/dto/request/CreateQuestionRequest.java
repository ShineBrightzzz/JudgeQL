package com.hainam.judgeql.question.dto.request;

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
public class CreateQuestionRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotBlank(message = "Language ID is required")
    private String languageId;
    
    private UUID testId;  // Optional
}
