package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.dto.auth.LoginRequest;
import com.nhnacademy.accountapi.dto.auth.LoginResponse;
import com.nhnacademy.accountapi.dto.user.*;
import com.nhnacademy.accountapi.entity.User;
import com.nhnacademy.accountapi.entity.UserRole;
import com.nhnacademy.accountapi.entity.UserStatus;
import com.nhnacademy.accountapi.exception.UserAlreadyExistsException;
import com.nhnacademy.accountapi.exception.UserBadRequestException;
import com.nhnacademy.accountapi.exception.UserNotAllowException;
import com.nhnacademy.accountapi.exception.UserNotFoundException;
import com.nhnacademy.accountapi.repository.UserRepository;
import com.nhnacademy.accountapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 전체 유저 조회
    @Override
    public List<UserResponse> getAllUsers(String requestId) {
        // 관리자(admin)만 전체 유저 조회 가능 -> 403 Forbidden
        if(!checkUserAdmin(requestId)) {
            throw new UserNotAllowException("관리자만 전체 유저를 조회할 수 있습니다.");
        }

        // 전체 유저 조회
        List<User> users=userRepository.findAll();

        // 응답 반환 User -> UserResponse
        return users.stream().map(
                u -> UserResponse.builder()
                        .userId(u.getId())
                        .email(u.getEmail())
                        .status(u.getStatus())
                        .role(u.getRole())
                        .build()
        ).toList();
    }

    // 유저 생성
    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest req) {
        // 이미 존재하는 유저인지 확인 -> 409 Conflict
        if(userRepository.existsById(req.userId())) {
            throw new UserAlreadyExistsException("이미 존재하는 유저입니다.");
        }

        // 비밀번호 암호화
        String password=passwordEncoder.encode(req.password());

        // 유저 생성
        User user=User.builder()
                .id(req.userId())
                .password(password)
                .email(req.email())
                .role(req.role())
                .build();

        // 유저 저장
        User savedUser=userRepository.save(user);

        // 응답 반환
        return UserResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .status(savedUser.getStatus())
                .role(savedUser.getRole())
                .build();
    }

    // 유저 단건 조회
    @Override
    public UserResponse getUser(String userId, String requestId) {
        // 관리자(admin)나 본인만 유저 정보 조회 가능 -> 403 Forbidden
        if(!checkUserAdmin(requestId) && !Objects.equals(userId, requestId)) {
            throw new UserNotAllowException("관리자나 본인만 유저 정보를 조회할 수 있습니다.");
        }

        // 유저 조회
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 응답 반환 User -> UserResponse
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }

    // 유저 정보 수정
    @Override
    @Transactional
    public UserResponse updateUser(String userId, String requestId, UserUpdateRequest req) {
        // 본인만 유저 정보 수정 가능 -> 403 Forbidden
        if(!Objects.equals(userId, requestId)) {
            throw new UserNotAllowException("본인만 유저 정보를 수정할 수 있습니다.");
        }

        // 유저 조회
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 비밀번호 암호화
        String password=passwordEncoder.encode(req.password());

        // 유저 정보 수정
        user.update(password, req.email(), req.status());

        // 응답 반환 User -> UserResponse
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }

    // 유저 삭제 -> 유저 상태 변경(DELETED)
    @Override
    @Transactional
    public void deleteUser(String userId, String requestId) {
        // 본인만 유저 삭제 가능 -> 403 Forbidden
        if(!Objects.equals(userId, requestId)) {
            throw new UserNotAllowException("본인만 유저를 삭제할 수 있습니다.");
        }

        // 유저 조회
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 유저 상태 변경
        user.setStatus(UserStatus.DELETED);
    }

    // 유저 상태 변경 (활성화/비활성화) -> ACTIVE/DORMANT, 관리자(admin)만 변경 가능
    @Override
    @Transactional
    public UserResponse changeUserStatus(String userId, String requestId, UserStatusUpdateRequest req) {
        // 관리자(admin)만 유저 상태 변경 가능 -> 403 Forbidden
        if(!checkUserAdmin(requestId)) {
            throw new UserNotAllowException("관리자만 유저 상태를 변경할 수 있습니다.");
        }

        // 변경할 상태 확인 -> 400 Bad Request
        if(req.status() != UserStatus.ACTIVE && req.status() != UserStatus.DORMANT) {
            throw new UserBadRequestException("유저 상태는 ACTIVE 또는 DORMANT만 가능합니다.");
        }

        // 유저 조회
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 유저 상태 변경
        user.setStatus(req.status());

        // 응답 반환 User -> UserResponse
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }

    // 로그인
    @Override
    public LoginResponse login(LoginRequest req) {

        // 유저 조회
        User user=userRepository.findById(req.userId())
                .orElseThrow(()-> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 비밀번호 확인
        if(!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }

        // 유저 상태가 ACTIVE인지 확인
        if(user.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotAllowException("유저 상태가 ACTIVE가 아닙니다.");
        }

        return LoginResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    // 유저 존재 여부 확인
    @Override
    public UserExistsResponse checkUserExists(String userId) {
        boolean exists=userRepository.existsById(userId);

        return new UserExistsResponse(exists);
    }

    // 유저가 관리자(admin)인지 확인
    private boolean checkUserAdmin(String userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("유저를 찾을 수 없습니다."));

        return Objects.equals(user.getRole(), UserRole.ADMIN);
    }
}
