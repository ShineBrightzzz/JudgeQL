package com.hainam.judgeql.grading.dto.response;

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
public class TestCaseResult {
    private Long testcaseId;
    private String status;
    private List<Map<String, Object>> actualOutput;
    private List<Map<String, Object>> expectedOutput;
    private String errorMessage;
}
