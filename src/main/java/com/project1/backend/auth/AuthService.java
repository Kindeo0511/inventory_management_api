package com.project1.backend.auth;

public interface AuthService {
    AuthResponse verify(AuthLoginRequest request);
    AuthResponse getRefreshToken(String refreshToken);
}
