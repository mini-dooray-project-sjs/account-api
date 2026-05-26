package com.nhnacademy.accountapi.dto.user;

import com.nhnacademy.accountapi.entity.UserStatus;

public record UserStatusUpdateRequest(
        UserStatus status
) {
}
