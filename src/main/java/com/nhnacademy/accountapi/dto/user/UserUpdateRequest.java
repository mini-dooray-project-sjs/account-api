package com.nhnacademy.accountapi.dto.user;

import com.nhnacademy.accountapi.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
        @Email
        String email,
        @NotNull
        String password,
        UserStatus status
) {
}
