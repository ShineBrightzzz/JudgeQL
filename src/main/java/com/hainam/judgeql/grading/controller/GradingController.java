package com.hainam.judgeql.grading.controller;

import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.shared.response.ApiResponse;
import com.hainam.judgeql.grading.dto.request.GradingRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class GradingController {
    
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> sumbitQuestion(@RequestBody GradingRequest gradingRequest) {
        
        
        
        return gradingRequest;
    }
    
}
