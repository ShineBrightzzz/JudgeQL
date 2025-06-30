package com.hainam.judgeql.grading.dto.response;

import java.util.List;
import java.util.UUID;

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
public class GradingResponse {
    private UUID submissionId;
    private String status;
    private int score;
    private int passedTestCount;
    private int totalTestCount;
    private List<TestCaseResult> testCaseResults;
}
