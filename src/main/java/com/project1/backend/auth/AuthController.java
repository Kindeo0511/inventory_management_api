package com.project1.backend.auth;

import com.project1.backend.payload.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponseData<AuthResponse>> login(@RequestBody AuthLoginRequest request){
        AuthResponse authResponse = authService.verify(request);
        ApiResponseData<AuthResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Login successfully",
                authResponse
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseData<AuthResponse>> refresh(@RequestParam String refreshToken){

        AuthResponse token = authService.getRefreshToken(refreshToken);
        ApiResponseData<AuthResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Token refresh successfully",
                token
        );
        return ResponseEntity.ok(response);
     
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken){
        authService.logOut(refreshToken);

        return ResponseEntity.ok("Logout Successful!");

    }



}
