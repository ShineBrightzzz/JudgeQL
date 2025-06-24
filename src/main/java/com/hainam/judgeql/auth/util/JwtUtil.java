package com.hainam.judgeql.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hainam.judgeql.user.domain.User;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyRaw;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // Convert secret string (base64 or plain) into proper Key object
        this.secretKey = Keys.hmacShaKeyFor(secretKeyRaw.getBytes());
    }

    public String generateToken(User user) {
        String fullName = user.getGivenName() + " " + user.getFamilyName();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRoleId())
                .claim("fullName", fullName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject().equals(user.getEmail()) &&
                   !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
