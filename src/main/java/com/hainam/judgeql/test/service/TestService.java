package com.hainam.judgeql.test.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.category.domain.Category;
import com.hainam.judgeql.category.service.CategoryService;
import com.hainam.judgeql.shared.exception.ResourceNotFoundException;
import com.hainam.judgeql.shared.response.MetaPage;
import com.hainam.judgeql.test.domain.Test;
import com.hainam.judgeql.test.dto.request.CreateTestRequest;
import com.hainam.judgeql.test.dto.response.TestResponse;
import com.hainam.judgeql.test.mapper.TestMapper;
import com.hainam.judgeql.test.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final CategoryService categoryService;

    public TestService(TestRepository testRepository, CategoryService categoryService) {
        this.testRepository = testRepository;
        this.categoryService = categoryService;
    }

    public TestResponse createTest(CreateTestRequest request) {
        Category category = categoryService.getCategoryById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Test test = TestMapper.toEntity(request, category);
        test = testRepository.save(test);
        
        return TestMapper.toResponse(test);
    }

    public TestResponse updateTest(UUID id, CreateTestRequest request) {
        Test test = getTestById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        
        Category category = categoryService.getCategoryById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        TestMapper.updateEntity(test, request, category);
        test = testRepository.save(test);
        
        return TestMapper.toResponse(test);
    }

    public TestResponse getTest(UUID id) {
        Test test = getTestById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        
        return TestMapper.toResponse(test);
    }

    public Optional<Test> getTestById(UUID id) {
        return testRepository.findById(id);
    }

    public void deleteTest(UUID id) {
        if (!testRepository.existsById(id)) {
            throw new ResourceNotFoundException("Test not found");
        }
        testRepository.deleteById(id);
    }

    public List<TestResponse> getAllTests(MetaPage pageInfo) {
        Sort.Direction direction = pageInfo.getSortDir() != null && pageInfo.getSortDir().equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), 
            Sort.by(direction, pageInfo.getSort()));
        
        Page<Test> testPage = testRepository.findAll(pageable);
        
        pageInfo.setTotalElements(testPage.getTotalElements());
        pageInfo.setTotalPages(testPage.getTotalPages());
        pageInfo.setFirst(testPage.isFirst());
        pageInfo.setLast(testPage.isLast());
        
        return testPage.getContent()
                .stream()
                .map(TestMapper::toResponse)
                .collect(Collectors.toList());
    }
}
