package com.hainam.judgeql.grading.dto.request;

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
public class GradingRequest {    
    private UUID submissionId;
    private String userId;
    private String questionId;
    private String userSql;
    private List<TestCaseGradingRequest> testCases;
}
