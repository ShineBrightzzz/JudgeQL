package com.hainam.judgeql.testcase.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseRequest {
    private String questionId;
    private String setupSql;
    private Object expectedOutput;
    private Boolean visible;
}
