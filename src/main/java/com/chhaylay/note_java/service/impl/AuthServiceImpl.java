package com.chhaylay.note_java.service.impl;

import com.chhaylay.note_java.dto.AuthDto;
import com.chhaylay.note_java.model.User;
import com.chhaylay.note_java.repository.UserRepository;
import com.chhaylay.note_java.security.JwtUtils;
import com.chhaylay.note_java.security.UserPrincipal;
import com.chhaylay.note_java.service.AuthService;
import com.chhaylay.note_java.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public AuthDto.Response register(AuthDto.Request request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);

        var userDetails = UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String jwtToken = jwtUtils.generateToken(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthDto.Response.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public AuthDto.Response login(AuthDto.Request request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var userDetails = UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String jwtToken = jwtUtils.generateToken(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthDto.Response.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public AuthDto.TokenRefreshResponse refreshToken(AuthDto.TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::validateForRefresh)
                .map(oldToken -> {
                    // Extract user info from the JWT string inside the record
                    String email = jwtUtils.extractEmail(oldToken.getToken());
                    Long userId = jwtUtils.extractUserId(oldToken.getToken());

                    // Mark old record as used
                    refreshTokenService.markAsRefreshed(oldToken);

                    // Build a shell user for the new token generation
                    User user = User.builder()
                            .id(userId)
                            .email(email)
                            .build();

                    var newToken = refreshTokenService.createRefreshToken(user);

                    var userDetails = UserPrincipal.builder()
                            .id(userId)
                            .email(email)
                            .build();
                    String accessToken = jwtUtils.generateToken(userDetails);

                    return AuthDto.TokenRefreshResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newToken.getToken())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
