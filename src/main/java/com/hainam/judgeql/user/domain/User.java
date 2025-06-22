package com.hainam.judgeql.user.domain;

import java.time.Instant;
import java.util.UUID;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verification_token_expires_at")
    private Instant verificationTokenExpiresAt;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;


    @PrePersist
    public void handleBeforeCreate(){
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedAt = Instant.now();
    }
}
