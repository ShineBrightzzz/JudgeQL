package com.hainam.judgeql.grading.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainam.judgeql.grading.domain.GradingResult;
import com.hainam.judgeql.grading.dto.response.GradingResponse;
import com.hainam.judgeql.grading.dto.response.TestCaseResult;

@Component
public class GradingMapper {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public GradingResult toEntity(GradingResponse response) {
        try {
            return GradingResult.builder()
                    .submissionId(response.getSubmissionId())
                    .score(response.getScore())
                    .passedTestCount(response.getPassedTestCount())
                    .totalTestCount(response.getTotalTestCount())
                    .testCaseResults(objectMapper.writeValueAsString(response.getTestCaseResults()))
                    .gradedAt(LocalDateTime.now())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting response to entity", e);
        }
    }
    
    public GradingResponse toResponse(GradingResult entity) {
        try {
            GradingResponse response = new GradingResponse();
            response.setSubmissionId(entity.getSubmissionId());
            response.setScore(entity.getScore());
            response.setPassedTestCount(entity.getPassedTestCount());
            response.setTotalTestCount(entity.getTotalTestCount());
            response.setStatus("done");
            
            // Convert JSON string back to test case results list
            List<TestCaseResult> testCaseResults = objectMapper.readValue(
                    entity.getTestCaseResults(),
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, TestCaseResult.class)
            );
            response.setTestCaseResults(testCaseResults);
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to response", e);
        }
    }
}
