package com.hainam.judgeql.grading.domain;

public enum TestResultStatus {
    PASS("pass"),
    FAIL("fail"),
    ERROR("error");
    
    private final String value;
    
    TestResultStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
