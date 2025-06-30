package com.hainam.judgeql.submission.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.hainam.judgeql.submission.domain.Submission;
import com.hainam.judgeql.submission.domain.SubmissionStatus;
import com.hainam.judgeql.submission.dto.request.SubmissionRequest;
import com.hainam.judgeql.submission.dto.response.SubmissionResponse;

public class SubmissionMapper {
    public static Submission toEntity(SubmissionRequest request, String userId) {
        return Submission.builder()
                .questionId(request.getQuestionId())
                .userId(userId)
                .input(request.getQuery())
                .status(SubmissionStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .build();
    }
    
    public static SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .questionId(submission.getQuestionId())
                .userId(submission.getUserId())
                .input(submission.getInput())
                .status(submission.getStatus())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
    
    public static List<SubmissionResponse> toResponseList(List<Submission> submissions) {
        return submissions.stream()
                .map(SubmissionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
