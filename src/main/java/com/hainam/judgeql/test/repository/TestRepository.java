package com.hainam.judgeql.test.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.test.domain.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {
}
