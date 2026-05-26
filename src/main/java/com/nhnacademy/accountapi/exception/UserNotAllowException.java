package com.nhnacademy.accountapi.exception;

// 403 Forbidden - 권한이 없는 사용자 (예: 비활성화된 사용자 또는 권한이 없는 사용자)
public class UserNotAllowException extends RuntimeException {
    public UserNotAllowException(String message) {
        super(message);
    }
}
