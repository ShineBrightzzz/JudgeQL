package com.hainam.judgeql.question.mapper;

import com.hainam.judgeql.question.domain.Question;
import com.hainam.judgeql.question.dto.request.CreateQuestionRequest;
import com.hainam.judgeql.question.dto.response.QuestionResponse;
import com.hainam.judgeql.test.domain.Test;
import com.hainam.judgeql.test.mapper.TestMapper;

public class QuestionMapper {
    public static Question toEntity(CreateQuestionRequest request) {
        return Question.builder()
                .name(request.getName())
                .content(request.getContent())
                .languageId(request.getLanguageId())
                .build();
    }

    public static QuestionResponse toResponse(Question question) {        QuestionResponse response = QuestionResponse.builder()
                .id(question.getId())
                .name(question.getName())
                .content(question.getContent())
                .languageId(question.getLanguageId())                .test(question.getTest() != null ? TestMapper.toResponse(question.getTest()) : null)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .createdBy(question.getCreatedBy())
                .updatedBy(question.getUpdatedBy())
                .build();

        return response;
    }

    public static void updateEntity(Question question, CreateQuestionRequest request) {
        question.setName(request.getName());
        question.setContent(request.getContent());
        question.setLanguageId(request.getLanguageId());
    }

    public static void setTest(Question question, Test test) {
        question.setTest(test);
    }
}
