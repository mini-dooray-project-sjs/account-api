package com.nhnacademy.accountapi.exception;

// 400 Bad Request - 잘못된 요청 (예: 유효하지 않은 입력 데이터)
public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
