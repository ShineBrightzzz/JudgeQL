package com.hainam.judgeql.submission.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.submission.domain.Submission;
import com.hainam.judgeql.submission.dto.request.SubmissionRequest;
import com.hainam.judgeql.submission.dto.response.SubmissionResponse;
import com.hainam.judgeql.submission.mapper.SubmissionMapper;
import com.hainam.judgeql.submission.repository.SubmissionRepository;
import com.hainam.judgeql.submission.service.SubmissionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Override
    public SubmissionResponse createSubmission(SubmissionRequest request, String userId) {
        Submission submission = SubmissionMapper.toEntity(request, userId);
        Submission savedSubmission = submissionRepository.save(submission);
        
        // TODO: Implement grading service integration in the future
        
        return SubmissionMapper.toResponse(savedSubmission);
    }

    @Override
    public SubmissionResponse getSubmissionById(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with id: " + id));
        
        return SubmissionMapper.toResponse(submission);
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByQuestionAndUser(String questionId, String userId) {
        List<Submission> submissions = submissionRepository.findByQuestionIdAndUserIdOrderBySubmittedAtDesc(questionId, userId);
        
        return SubmissionMapper.toResponseList(submissions);
    }
}