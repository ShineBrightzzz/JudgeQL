package com.hainam.judgeql.testcase.dto.request;

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
public class TestCaseRequest {
    private String questionId;
    private String setupSql;
    private List<Map<String, Object>> expectedOutput;
    private Boolean visible;
}
