package com.project1.backend.payload;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseData<T> {
    private boolean success;
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
}
