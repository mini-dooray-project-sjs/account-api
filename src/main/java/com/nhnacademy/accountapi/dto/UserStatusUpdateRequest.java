package com.nhnacademy.accountapi.dto;

import com.nhnacademy.accountapi.entity.UserStatus;

public record UserStatusUpdateRequest(
        UserStatus status
) {
}
