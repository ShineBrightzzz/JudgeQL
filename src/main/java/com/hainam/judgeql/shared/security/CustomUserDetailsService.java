package com.hainam.judgeql.shared.security;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new UsernameNotFoundException("user '" + id + "' not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            user.getPassword(),
            user.getEmailVerified() != null && user.getEmailVerified(), // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            new ArrayList<>() // authorities
        );
    }
}
