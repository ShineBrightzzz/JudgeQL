package com.hainam.judgeql.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.dto.response.UserResponse;
import com.hainam.judgeql.user.mapper.UserMapper;
import com.hainam.judgeql.user.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(User user){
        return userRepository.save(user);
    }
    
}
