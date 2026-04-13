package com.docker.setup.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    // --- Response Details (Metadata) ---
    private LocalDateTime timestamp;
    private boolean success;
    private int status;
    private String message; // General success or descriptive message

    // --- Data Payload ---
    private T data; // Generic type to hold the actual response data (e.g., User, List<User>)

    // --- Factory Method for Success ---
    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                true,
                status.value(),
                message,
                data
        );
    }
}