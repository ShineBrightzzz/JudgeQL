package com.hainam.judgeql.submission.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {    
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String questionId;

    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;
    
    @Column(nullable = false)
    private LocalDateTime submittedAt;
}
