package com.nhnacademy.accountapi.dto;

import com.nhnacademy.accountapi.entity.UserRole;
import com.nhnacademy.accountapi.entity.UserStatus;
import lombok.Builder;

@Builder
public record UserLoginResponse(
        String id,
        String password,
        UserStatus status,
        UserRole role
){
}
