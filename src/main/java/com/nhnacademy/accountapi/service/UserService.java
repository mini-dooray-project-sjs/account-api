package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.dto.*;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(String requesterId);

    UserResponse createUser(UserCreateRequest req);

    // 유저 단건 조회
    UserResponse getUser(String userId, String requestId);

    UserResponse updateUser(String userId, String requestId, UserUpdateRequest req);

    // 유저 삭제 -> 유저 상태 변경(DELETED)
    void deleteUser(String userId, String requestId);

    UserResponse changeUserStatus(String userId, String requestId, UserStatusUpdateRequest req);

    UserExistsResponse checkUserExists(String userId);

    // 로그인 관련 정보 조회
    UserLoginResponse getLoginInfo(String userId);
}
