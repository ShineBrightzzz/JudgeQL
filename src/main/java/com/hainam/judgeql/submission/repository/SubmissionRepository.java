package com.hainam.judgeql.submission.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.submission.domain.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findByQuestionIdAndUserIdOrderBySubmittedAtDesc(String questionId, String userId);
}
