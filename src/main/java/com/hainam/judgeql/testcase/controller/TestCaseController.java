package com.hainam.judgeql.testcase.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.testcase.dto.request.TestCaseRequest;
import com.hainam.judgeql.testcase.dto.response.TestCaseResponse;
import com.hainam.judgeql.testcase.service.TestCaseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseService testCaseService;
    
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<TestCaseResponse>> findByQuestionId(@PathVariable String questionId) {
        return ResponseEntity.ok(testCaseService.findByQuestionId(questionId));
    }
    
    @GetMapping("/question/{questionId}/visible")
    public ResponseEntity<List<TestCaseResponse>> findVisibleByQuestionId(@PathVariable String questionId) {
        return ResponseEntity.ok(testCaseService.findVisibleByQuestionId(questionId));
    }
    
    @PostMapping
    public ResponseEntity<TestCaseResponse> create(@RequestBody TestCaseRequest request) {
        return new ResponseEntity<>(testCaseService.create(request), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseResponse> update(@PathVariable String id, @RequestBody TestCaseRequest request) {
        return ResponseEntity.ok(testCaseService.update(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        testCaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
