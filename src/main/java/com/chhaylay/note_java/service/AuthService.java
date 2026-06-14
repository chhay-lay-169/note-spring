package com.chhaylay.note_java.service;

import com.chhaylay.note_java.dto.AuthDto;

public interface AuthService {
    AuthDto.Response register(AuthDto.Request request);
    AuthDto.Response login(AuthDto.Request request);
    AuthDto.TokenRefreshResponse refreshToken(AuthDto.TokenRefreshRequest request);
}
