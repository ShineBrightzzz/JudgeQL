package com.hainam.judgeql.testcase.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
    private List<Map<String, Object>> expectedOutput;
    private Boolean visible;
    private Instant createdAt;
    private Instant updatedAt;
}
