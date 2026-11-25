package com.project1.backend.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthLoginRequest {
    @NotBlank
    public String email;
    @NotBlank
    public String password;
}
