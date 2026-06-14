package com.chhaylay.note_java.exception;

import com.chhaylay.note_java.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Environment env;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleAllExceptions(Exception ex) {
        return buildErrorResponse(ex, "An unexpected error occurred");
    }

    private ApiResponse<Void> buildErrorResponse(Exception ex, String message) {
        String stackTrace = null;
        if (isDev()) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            stackTrace = sw.toString();
        }
        return ApiResponse.error(message, stackTrace);
    }

    private boolean isDev() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }
}
