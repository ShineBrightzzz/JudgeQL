package com.hainam.judgeql.question.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.question.domain.Question;
import com.hainam.judgeql.question.dto.request.CreateQuestionRequest;
import com.hainam.judgeql.question.dto.response.QuestionResponse;
import com.hainam.judgeql.question.mapper.QuestionMapper;
import com.hainam.judgeql.question.repository.QuestionRepository;
import com.hainam.judgeql.shared.exception.ResourceNotFoundException;
import com.hainam.judgeql.shared.response.MetaPage;
import com.hainam.judgeql.test.domain.Test;
import com.hainam.judgeql.test.service.TestService;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TestService testService;

    public QuestionService(QuestionRepository questionRepository, TestService testService) {
        this.questionRepository = questionRepository;
        this.testService = testService;
    }

    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question = QuestionMapper.toEntity(request);
        
        if (request.getTestId() != null) {
            Test test = testService.getTestById(request.getTestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
            QuestionMapper.setTest(question, test);
        }
        
        question = questionRepository.save(question);
        return QuestionMapper.toResponse(question);
    }

    public QuestionResponse updateQuestion(UUID id, CreateQuestionRequest request) {
        Question question = getQuestionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        QuestionMapper.updateEntity(question, request);
        
        if (request.getTestId() != null) {
            Test test = testService.getTestById(request.getTestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
            QuestionMapper.setTest(question, test);
        } else {
            QuestionMapper.setTest(question, null);
        }

        question = questionRepository.save(question);
        return QuestionMapper.toResponse(question);
    }

    public QuestionResponse getQuestion(UUID id) {
        Question question = getQuestionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return QuestionMapper.toResponse(question);
    }

    public Optional<Question> getQuestionById(UUID id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found");
        }
        questionRepository.deleteById(id);
    }

    public List<QuestionResponse> getAllQuestions(MetaPage pageInfo) {
        Sort.Direction direction = pageInfo.getSortDir() != null && pageInfo.getSortDir().equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), 
            Sort.by(direction, pageInfo.getSort()));
        
        Page<Question> questionPage = questionRepository.findAll(pageable);
        
        pageInfo.setTotalElements(questionPage.getTotalElements());
        pageInfo.setTotalPages(questionPage.getTotalPages());
        pageInfo.setFirst(questionPage.isFirst());
        pageInfo.setLast(questionPage.isLast());
        
        return questionPage.getContent()
                .stream()
                .map(QuestionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
