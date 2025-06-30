package com.hainam.judgeql.grading.dto.request;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseGradingRequest {
    private Long testcaseId;
    private String setupSql;
    private List<Map<String, Object>> expectedOutput;
}
