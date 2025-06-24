package com.hainam.judgeql.category.service;

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
import com.hainam.judgeql.category.dto.request.CreateCategoryRequest;
import com.hainam.judgeql.category.dto.response.CategoryResponse;
import com.hainam.judgeql.category.mapper.CategoryMapper;
import com.hainam.judgeql.category.repository.CategoryRepository;
import com.hainam.judgeql.shared.exception.ResourceNotFoundException;
import com.hainam.judgeql.shared.response.MetaPage;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = CategoryMapper.toEntity(request);
        category = categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(UUID id, CreateCategoryRequest request) {
        Category category = getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryMapper.updateEntity(category, request);
        category = categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse getCategory(UUID id) {
        Category category = getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return CategoryMapper.toResponse(category);
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryResponse> getAllCategories(MetaPage pageInfo) {
        Sort.Direction direction = pageInfo.getSortDir() != null && pageInfo.getSortDir().equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), 
            Sort.by(direction, pageInfo.getSort()));
        
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        
        pageInfo.setTotalElements(categoryPage.getTotalElements());
        pageInfo.setTotalPages(categoryPage.getTotalPages());
        pageInfo.setFirst(categoryPage.isFirst());
        pageInfo.setLast(categoryPage.isLast());
        
        return categoryPage.getContent()
                .stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
