package com.hainam.judgeql.category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hainam.judgeql.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
