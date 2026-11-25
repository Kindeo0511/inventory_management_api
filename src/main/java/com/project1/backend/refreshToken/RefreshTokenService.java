package com.project1.backend.refreshToken;

import com.project1.backend.User.UserRepository;
import com.project1.backend.User.Users;
import com.project1.backend.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.expiration-days}")
    private long refreshTokenExpiryDays;

    public RefreshToken createRefreshToken(String email){
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("User not found with email: "+ email)
                );

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenExpiryDays))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->
                        new InvalidRefreshTokenException("Invalid refresh token")
                );

        if(refreshToken.isRevoked()){
            throw  new InvalidRefreshTokenException("Refresh token has been revoked");
        }

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw  new InvalidRefreshTokenException("Refresh token has expired");
        }

        return refreshToken;
    }


    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken){
        oldRefreshToken.setRevoked(true);
        refreshTokenRepository.save(oldRefreshToken);

        return createRefreshToken(oldRefreshToken.getUser().getEmail());
    }

    @Transactional
    public void revokedRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow( () ->
                        new InvalidRefreshTokenException("Invalid refresh token")
                );
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

    }
}
