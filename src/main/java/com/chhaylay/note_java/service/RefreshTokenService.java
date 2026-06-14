package com.chhaylay.note_java.service;

import com.chhaylay.note_java.model.RefreshToken;
import com.chhaylay.note_java.model.User;
import com.chhaylay.note_java.repository.RefreshTokenRepository;
import com.chhaylay.note_java.security.JwtUtils;
import com.chhaylay.note_java.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        String token = jwtUtils.generateRefreshToken(userPrincipal);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .isRefreshed(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateForRefresh(RefreshToken token) {
        // 1. Check if it was already used
        if (token.isRefreshed()) {
            throw new RuntimeException("Refresh token was already used!");
        }

        // 2. Check JWT expiration/validity
        try {
            jwtUtils.extractEmail(token.getToken());
        } catch (Exception e) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token is expired or invalid. Please login again.");
        }

        return token;
    }

    @Transactional
    public void markAsRefreshed(RefreshToken token) {
        token.setRefreshed(true);
        refreshTokenRepository.save(token);
    }
}
