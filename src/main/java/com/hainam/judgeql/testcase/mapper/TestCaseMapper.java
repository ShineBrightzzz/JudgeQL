package com.hainam.judgeql.testcase.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.hainam.judgeql.testcase.domain.TestCase;
import com.hainam.judgeql.testcase.dto.request.TestCaseRequest;
import com.hainam.judgeql.testcase.dto.response.TestCaseResponse;

@Component
public class TestCaseMapper {
    
    public TestCase toEntity(TestCaseRequest request) {
        return TestCase.builder()
                .questionId(UUID.fromString(request.getQuestionId()))
                .setupSql(request.getSetupSql())
                .expectedOutput(request.getExpectedOutput())
                .visible(request.getVisible())
                .build();
    }
    
    public TestCaseResponse toResponse(TestCase testCase) {
        return TestCaseResponse.builder()
                .id(testCase.getId().toString())
                .questionId(testCase.getQuestionId().toString())
                .setupSql(testCase.getSetupSql())
                .expectedOutput(testCase.getExpectedOutput())
                .visible(testCase.getVisible())
                .createdAt(testCase.getCreatedAt())
                .updatedAt(testCase.getUpdatedAt())
                .build();
    }
}
