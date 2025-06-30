package com.hainam.judgeql.grading.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.grading.domain.GradingResult;

@Repository
public interface GradingResultRepository extends JpaRepository<GradingResult, UUID> {
    Optional<GradingResult> findBySubmissionId(UUID submissionId);
}
