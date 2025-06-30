package com.hainam.judgeql.testcase.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hainam.judgeql.testcase.domain.TestCase;
import com.hainam.judgeql.testcase.dto.request.TestCaseRequest;
import com.hainam.judgeql.testcase.dto.response.TestCaseResponse;
import com.hainam.judgeql.testcase.exception.TestCaseNotFoundException;
import com.hainam.judgeql.testcase.mapper.TestCaseMapper;
import com.hainam.judgeql.testcase.repository.TestCaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;
    
    @Transactional(readOnly = true)
    public List<TestCaseResponse> findByQuestionId(String questionId) {
        return testCaseRepository.findByQuestionId(UUID.fromString(questionId))
                .stream()
                .map(testCaseMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TestCaseResponse> findVisibleByQuestionId(String questionId) {
        return testCaseRepository.findByQuestionIdAndVisible(UUID.fromString(questionId), true)
                .stream()
                .map(testCaseMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TestCaseResponse create(TestCaseRequest request) {
        TestCase testCase = testCaseMapper.toEntity(request);
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toResponse(testCase);
    }
      @Transactional
    public TestCaseResponse update(String id, TestCaseRequest request) {
        TestCase testCase = testCaseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TestCaseNotFoundException(id));
        
        testCase.setQuestionId(UUID.fromString(request.getQuestionId()));
        testCase.setSetupSql(request.getSetupSql());
        testCase.setExpectedOutput(request.getExpectedOutput());
        testCase.setVisible(request.getVisible());
        
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toResponse(testCase);
    }
    
    @Transactional
    public void delete(String id) {
        testCaseRepository.deleteById(UUID.fromString(id));
    }
}
