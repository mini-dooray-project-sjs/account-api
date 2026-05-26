package com.nhnacademy.accountapi.dto.user;

import com.nhnacademy.accountapi.entity.UserRole;
import com.nhnacademy.accountapi.entity.UserStatus;
import lombok.Builder;

@Builder
public record UserResponse(
        String userId,
        String email,
        UserStatus status,
        UserRole role
) {
}
