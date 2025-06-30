package com.hainam.judgeql.testcase.dto.response;

import java.time.Instant;

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
public class TestCaseResponse {
    private String id;
    private String questionId;
    private String setupSql;
    private Object expectedOutput;
    private Boolean visible;
    private Instant createdAt;
    private Instant updatedAt;
}
