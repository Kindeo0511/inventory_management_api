package com.project1.backend.auth;

import com.project1.backend.User.UserRepository;
import com.project1.backend.refreshToken.RefreshToken;
import com.project1.backend.refreshToken.RefreshTokenService;
import com.project1.backend.security.JWTService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final JWTService jwtService;
    private final AuthenticationManager authManager;

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    @Override
    public AuthResponse verify(AuthLoginRequest request) {
        log.info("User logging in");
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        if(!authentication.isAuthenticated()){
            System.out.println("Failed");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        log.info("Successfully log in");
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

    }

    @Override
    public AuthResponse getRefreshToken(String refreshToken) {
            RefreshToken verifiedToken = refreshTokenService.verifyRefreshToken(refreshToken);

            UserDetails userDetails = User.builder()
                    .username(verifiedToken.getUser().getEmail())
                    .password(verifiedToken.getUser().getPassword())
                    .roles(verifiedToken.getUser().getRole().name())
                    .build();

            String newAccessToken = jwtService.generateToken(userDetails);
            RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(verifiedToken);

            return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();

    }

    public void logOut(String refreshToken){
        refreshTokenService.revokedRefreshToken(refreshToken);
    }
}
