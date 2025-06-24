package com.hainam.judgeql.question.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.hainam.judgeql.test.dto.response.TestResponse;

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
public class QuestionResponse {    private UUID id;
    private String name;
    private String content;
    private String languageId;
    private TestResponse test;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
