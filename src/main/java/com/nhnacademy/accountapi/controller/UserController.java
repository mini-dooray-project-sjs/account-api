package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.user.*;
import com.nhnacademy.accountapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 유저 리스트 조회 -> X-User-Id 확인 -> 관리자만 조회 가능하도록
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestHeader("X-User-Id") String requesterId
    ) {
        List<UserResponse> resp=userService.getAllUsers(requesterId);

        return ResponseEntity.ok(resp);
    }

    // 유저 생성 -> 회원가입
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest req
    ) {
        UserResponse resp=userService.createUser(req);

        return ResponseEntity.ok(resp);
    }

    // 유저 단건 조회 -> X-User-Id 확인이 필요할까? -> 남의 정보를 봐야할수도?
    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("user-id") String userId,
            @RequestHeader("X-User-Id") String requesterId
    ) {
        UserResponse resp=userService.getUser(userId, requesterId);

        return ResponseEntity.ok(resp);
    }

    // 유저 정보 수정 -> X-User-Id 확인 필요 (본인만 수정 가능)
    @PostMapping("/{user-id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("user-id") String userId,
            @RequestHeader("X-User-Id") String requesterId,
            @Valid @RequestBody UserUpdateRequest req
    ) {
        UserResponse resp=userService.updateUser(userId, requesterId, req);

        return ResponseEntity.ok(resp);
    }

    // 유저 삭제 -> 유저 상태 변경(DELETED) -> X-User-Id 확인 필요 (본인만 삭제 가능)
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id") String userId,
            @RequestHeader("X-User-Id") String requesterId
    ) {
        userService.deleteUser(userId, requesterId);

        return ResponseEntity.noContent().build();
    }

    // 유저 상태 변경 (활성화/비활성화) -> ACTIVE/DORMANT -> X-User-Id 확인 필요 (관리자만 변경 가능)
    @PostMapping("/{user-id}/status")
    public ResponseEntity<UserResponse> changeUserStatus(
            @PathVariable("user-id") String userId,
            @RequestHeader("X-User-Id") String requesterId,
            @Valid @RequestBody UserStatusUpdateRequest req
    ) {
        UserResponse resp=userService.changeUserStatus(userId, requesterId, req);

        return ResponseEntity.ok(resp);
    }

    // 유저 존재 여부 확인
    @GetMapping("/{user-id}/exists")
    public ResponseEntity<UserExistsResponse> checkUserExists(
            @PathVariable("user-id") String userId
    ) {
        UserExistsResponse resp=userService.checkUserExists(userId);

        return ResponseEntity.ok(resp);
    }
}
