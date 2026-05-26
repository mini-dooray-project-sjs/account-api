package com.nhnacademy.accountapi.exception;

// 404 Not Found - 리소스를 찾을 수 없음 (예: 사용자 ID가 존재하지 않을 때)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
