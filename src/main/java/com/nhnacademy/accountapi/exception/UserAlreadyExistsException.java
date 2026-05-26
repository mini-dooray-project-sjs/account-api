package com.nhnacademy.accountapi.exception;


// 409 Conflict - 리소스 충돌 (예: 이미 존재하는 사용자 ID)
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }


}
