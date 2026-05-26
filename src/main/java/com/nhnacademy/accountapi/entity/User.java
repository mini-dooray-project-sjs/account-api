package com.nhnacademy.accountapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;

@Getter @Setter
@Entity @Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @Column(name="user_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @NotNull
    @Column(name="password", nullable = false)
    private String password;

    @NotNull
    @Column(name="status", nullable = false)
    private UserStatus status;

    @NotNull
    @Column(name="role", nullable = false)
    private UserRole role= UserRole.USER;

    @Email
    @Column(name="email", nullable = true)
    private String email;

    @NotNull
    @Column(name="created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Builder
    public User(String id, String password, String email, UserRole role) {
        this.id=id;
        this.password=password;
        this.email=email;
        this.status=UserStatus.ACTIVE;
        this.createdAt=ZonedDateTime.now();
        this.role=role;
    }

    public void update(String password, String email, UserStatus status) {
        this.password=password;
        this.email=email;
        this.status=status;
    }
}
