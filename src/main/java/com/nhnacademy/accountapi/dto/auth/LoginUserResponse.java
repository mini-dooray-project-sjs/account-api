package com.nhnacademy.accountapi.dto.auth;

import com.nhnacademy.accountapi.entity.UserRole;
import lombok.Builder;

@Builder
public record LoginUserResponse(
        String userId,
        UserRole role
) {
}
