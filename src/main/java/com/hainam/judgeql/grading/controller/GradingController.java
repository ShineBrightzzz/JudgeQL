package com.hainam.judgeql.grading.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.grading.dto.request.GradingRequest;
import com.hainam.judgeql.grading.dto.response.GradingResponse;
import com.hainam.judgeql.grading.service.GradingService;
import com.hainam.judgeql.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/grading")
public class GradingController {
    
    private final GradingService gradingService;
    
    @Autowired
    public GradingController(GradingService gradingService) {
        this.gradingService = gradingService;
    }
      @PostMapping("/manual")
    public ResponseEntity<ApiResponse<GradingResponse>> gradeSubmission(@RequestBody GradingRequest request) {
        GradingResponse response = gradingService.gradeSubmission(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Submission graded successfully"));
    }@GetMapping("/submissions/{submissionId}")
    public ResponseEntity<ApiResponse<GradingResponse>> getGradingResult(@PathVariable UUID submissionId) {
        return gradingService.getGradingResultBySubmissionId(submissionId)
                .map(response -> ResponseEntity.ok(ApiResponse.success(response, "Grading result retrieved successfully")))
                .orElse(ResponseEntity.notFound().build());
    }
}
