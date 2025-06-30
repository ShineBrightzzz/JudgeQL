package com.hainam.judgeql.grading.service;

import com.hainam.judgeql.grading.dto.request.GradingRequest;

public interface GradingService {
    void gradeSubmission(GradingRequest request);
}
