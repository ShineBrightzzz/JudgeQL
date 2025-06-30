package com.hainam.judgeql.grading.service;

import java.util.Optional;
import java.util.UUID;

import com.hainam.judgeql.grading.dto.request.GradingRequest;
import com.hainam.judgeql.grading.dto.response.GradingResponse;

public interface GradingService {
    GradingResponse gradeSubmission(GradingRequest request);
    void processSubmission(UUID submissionId);
    Optional<GradingResponse> getGradingResultBySubmissionId(UUID submissionId);
}
