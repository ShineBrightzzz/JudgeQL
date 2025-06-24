package com.hainam.judgeql.test.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.shared.response.ApiResponse;
import com.hainam.judgeql.shared.response.MetaPage;
import com.hainam.judgeql.test.dto.request.CreateTestRequest;
import com.hainam.judgeql.test.dto.response.TestResponse;
import com.hainam.judgeql.test.service.TestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }    
    
    @PostMapping
    public ResponseEntity<ApiResponse<TestResponse>> createTest(
            @Valid @RequestBody CreateTestRequest request) {
        TestResponse response = testService.createTest(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Test created successfully"));
    }    
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TestResponse>> updateTest(
            @PathVariable UUID id,
            @Valid @RequestBody CreateTestRequest request) {
        TestResponse response = testService.updateTest(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Test updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestResponse>> getTest(@PathVariable UUID id) {
        TestResponse response = testService.getTest(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Test retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTest(@PathVariable UUID id) {
        testService.deleteTest(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Test deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestResponse>>> getAllTests(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String sortDir) {
        MetaPage metaPage = new MetaPage();
        metaPage.setPage(page);
        metaPage.setSize(size);
        metaPage.setSort(sort);
        metaPage.setSortDir(sortDir);
        
        List<TestResponse> response = testService.getAllTests(metaPage);
        return ResponseEntity.ok(ApiResponse.success(response, "Tests retrieved successfully", metaPage));
    }
}
