package com.hainam.judgeql.submission.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.shared.response.ApiResponse;
import com.hainam.judgeql.shared.security.SecurityUtils;
import com.hainam.judgeql.submission.dto.request.SubmissionRequest;
import com.hainam.judgeql.submission.dto.response.SubmissionResponse;
import com.hainam.judgeql.submission.service.SubmissionService;

@RestController
public class SubmissionController {
    
    @Autowired
    private SubmissionService submissionService;    // POST /submissions - Submit a SQL query for grading
    @PostMapping("/submissions")
    public ResponseEntity<ApiResponse<SubmissionResponse>> createSubmission(@RequestBody SubmissionRequest submissionRequest) {
        // Get current authenticated user's ID using the utility method
        String userId = SecurityUtils.getCurrentUserId();
        
        SubmissionResponse submission = submissionService.createSubmission(submissionRequest, userId);
        
        ApiResponse<SubmissionResponse> response = ApiResponse.success(
            submission, 
            "Submission created successfully"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // GET /submissions/:id - Get details of a specific submission
    @GetMapping("/submissions/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmission(@PathVariable UUID id) {
        SubmissionResponse submission = submissionService.getSubmissionById(id);
        
        ApiResponse<SubmissionResponse> response = ApiResponse.success(
            submission, 
            "Submission retrieved successfully"
        );
        
        return ResponseEntity.ok(response);
    }    // GET /questions/:questionId/submissions - Get user's submissions for a question
    @GetMapping("/questions/{questionId}/submissions")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getSubmissionsByQuestion(@PathVariable String questionId) {
        // Get current authenticated user's ID using the utility method
        String userId = SecurityUtils.getCurrentUserId();
        
        List<SubmissionResponse> submissions = submissionService.getSubmissionsByQuestionAndUser(questionId, userId);
        
        ApiResponse<List<SubmissionResponse>> response = ApiResponse.success(
            submissions, 
            "Submissions retrieved successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}
