package com.hainam.judgeql.question.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.question.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
