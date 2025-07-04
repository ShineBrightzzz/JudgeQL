package com.hainam.judgeql.auth.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hainam.judgeql.user.domain.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600}")
    private long jwtExpirationInSec;

    @Value("${jwt.refresh-expiration:604800}") // 7 days by default
    private long refreshTokenExpirationInSec;

    public String generateToken(User user) {
        return generateToken(user, jwtExpirationInSec);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpirationInSec);
    }

    private String generateToken(User user, long expirationInSec) {
        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("roleId", user.getRoleId());
        // Add more claims if needed

        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKeySpec key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(java.util.Date.from(now))
                .setExpiration(java.util.Date.from(now.plus(expirationInSec, ChronoUnit.SECONDS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> validateTokenAndGetClaims(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKeySpec key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
