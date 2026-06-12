package com.chhaylay.note_java.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

public final class NoteDto
{
    private NoteDto() {}

    @Data
    public static class Request {
        @NotBlank(message = "Title field must not be empty.")
        @Size(max = 150, message = "Title cannot exceed 150 characters.")
        private String title;

        private String content;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ResponseAll {
        private Long id;
        private String title;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ResponseDetail {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime updatedAt;
    }
}