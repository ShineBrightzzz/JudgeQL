package com.hainam.judgeql.user.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.dto.response.UserResponse;
import com.hainam.judgeql.user.mapper.UserMapper;
import com.hainam.judgeql.user.repository.UserRepository;
import com.hainam.judgeql.shared.response.MetaPage;

@Service
public class UserService {    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(MetaPage pageInfo){
        // Create sort direction
        Sort.Direction direction = pageInfo.getSortDir() != null && pageInfo.getSortDir().equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        // Create pageable
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), 
            Sort.by(direction, pageInfo.getSort()));
        
        // Get page of users
        Page<User> userPage = userRepository.findAll(pageable);
        
        // Update meta info
        pageInfo.setTotalElements(userPage.getTotalElements());
        pageInfo.setTotalPages(userPage.getTotalPages());
        pageInfo.setFirst(userPage.isFirst());
        pageInfo.setLast(userPage.isLast());
        
        // Map and return users
        return userPage.getContent()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(User user){
        return userRepository.save(user);
    }
    
}
