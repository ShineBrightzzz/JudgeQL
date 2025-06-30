package com.hainam.judgeql.submission.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hainam.judgeql.submission.domain.SubmissionStatus;

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
public class SubmissionResponse {
    private UUID id;
    private String questionId;
    private String userId;
    private String input;
    private SubmissionStatus status;
    private LocalDateTime submittedAt;
}
