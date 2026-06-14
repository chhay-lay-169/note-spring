package com.chhaylay.note_java.controller;

import com.chhaylay.note_java.dto.ApiResponse;
import com.chhaylay.note_java.dto.AuthDto;
import com.chhaylay.note_java.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthDto.Response> register(@Valid @RequestBody AuthDto.Request request) {
        return ApiResponse.success("User registered successfully", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthDto.Response> login(@Valid @RequestBody AuthDto.Request request) {
        return ApiResponse.success("Login successful", authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthDto.TokenRefreshResponse> refreshToken(@Valid @RequestBody AuthDto.TokenRefreshRequest request) {
        return ApiResponse.success("Token refreshed successfully", authService.refreshToken(request));
    }
}
