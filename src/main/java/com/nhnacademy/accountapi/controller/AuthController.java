package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.auth.LoginRequest;
import com.nhnacademy.accountapi.dto.auth.LoginResponse;
import com.nhnacademy.accountapi.dto.auth.LoginUserResponse;
import com.nhnacademy.accountapi.entity.UserRole;
import com.nhnacademy.accountapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (
            @Valid @RequestBody LoginRequest req,
            HttpSession session
    ) {
        LoginResponse resp=userService.login(req);

        session.setAttribute("X-User-Id", resp.userId());
        session.setAttribute("X-User-Role", resp.role());

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginUserResponse> getCurrentUser(
            @RequestHeader(value="X-User-Id", required=false) String userId,
            @RequestHeader(value="X-User-Role", required=false) String roleStr
    ) {
        if(roleStr.contains("ROLE_")) {
            roleStr=roleStr.replace("ROLE_", "");
        }
        UserRole role=UserRole.valueOf(roleStr);

        LoginUserResponse resp=LoginUserResponse.builder()
                .userId(userId)
                .role(role)
                .build();

        return ResponseEntity.ok(resp);
    }
}
