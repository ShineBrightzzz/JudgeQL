package com.hainam.judgeql.testcase.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.testcase.domain.TestCase;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findByQuestionId(UUID questionId);
    List<TestCase> findByQuestionIdAndVisible(UUID questionId, Boolean visible);
}
