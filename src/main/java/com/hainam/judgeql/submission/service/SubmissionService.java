package com.hainam.judgeql.submission.service;

import java.util.List;
import java.util.UUID;

import com.hainam.judgeql.submission.dto.request.SubmissionRequest;
import com.hainam.judgeql.submission.dto.response.SubmissionResponse;

public interface SubmissionService {
    SubmissionResponse createSubmission(SubmissionRequest request, String userId);
    SubmissionResponse getSubmissionById(UUID id);
    List<SubmissionResponse> getSubmissionsByQuestionAndUser(String questionId, String userId);
}
