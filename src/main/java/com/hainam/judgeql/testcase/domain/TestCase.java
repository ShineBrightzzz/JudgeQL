package com.hainam.judgeql.testcase.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hainam.judgeql.shared.domain.BaseEntity;
import com.hainam.judgeql.shared.util.JsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCase extends BaseEntity {
    @Column(name = "question_id", nullable = false)
    private UUID questionId;    @Column(name = "setup_sql", columnDefinition = "TEXT")
    private String setupSql;
      @Column(name = "expected_output", columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private List<Map<String, Object>> expectedOutput;
    
    @Column(name = "visible", nullable = false)
    private Boolean visible;
}
