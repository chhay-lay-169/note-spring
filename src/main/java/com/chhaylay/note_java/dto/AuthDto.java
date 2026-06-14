package com.chhaylay.note_java.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank
        @Email
        @Size(min = 3, max = 100)
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String token;
        private String refreshToken;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenRefreshRequest {
        @NotBlank
        private String refreshToken;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenRefreshResponse {
        private String accessToken;
        private String refreshToken;
    }
}
