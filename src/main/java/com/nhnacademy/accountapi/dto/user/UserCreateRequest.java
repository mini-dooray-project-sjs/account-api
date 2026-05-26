package com.nhnacademy.accountapi.dto.user;

import com.nhnacademy.accountapi.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull
        String userId,
        @Email
        String email,
        @NotNull
        String password,
        @NotNull
        UserRole role
) {
}
