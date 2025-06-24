package com.hainam.judgeql.auth.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hainam.judgeql.auth.domain.RefreshToken;
import com.hainam.judgeql.auth.exception.TokenRefreshException;
import com.hainam.judgeql.auth.repository.RefreshTokenRepository;
import com.hainam.judgeql.user.domain.User;

@Service
public class RefreshTokenService {
    @Value("${jwt.refresh-expiration:604800}")
    private Long refreshTokenExpirationInSec;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusSeconds(refreshTokenExpirationInSec))
            .revoked(false)
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0 || Boolean.TRUE.equals(token.getRevoked())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired or revoked. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
    
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenRefreshException(token, "Refresh token not found"));
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }
}
