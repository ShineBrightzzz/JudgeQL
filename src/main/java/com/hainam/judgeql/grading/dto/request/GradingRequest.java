package com.hainam.judgeql.grading.dto.request;

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
public class GradingRequest {    private String questionId;
    private String userId;
    private String query;
    private String submissionId;
    private String input;
}
