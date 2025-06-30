package com.hainam.judgeql.testcase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TestCaseNotFoundException extends RuntimeException {
    public TestCaseNotFoundException(String id) {
        super("TestCase not found with id: " + id);
    }
}
