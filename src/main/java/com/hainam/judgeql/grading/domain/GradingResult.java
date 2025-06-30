package com.hainam.judgeql.grading.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.UuidGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradingResult {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID submissionId;
    
    @Column(nullable = false)
    private int score;
    
    @Column(nullable = false)
    private int passedTestCount;
    
    @Column(nullable = false)
    private int totalTestCount;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String testCaseResults; // JSON string of test case results
    
    @Column(nullable = false)
    private LocalDateTime gradedAt;
}
