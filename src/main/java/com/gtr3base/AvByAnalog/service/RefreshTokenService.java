package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.AuthResponse;
import com.gtr3base.AvByAnalog.dto.RefreshTokenRequest;
import com.gtr3base.AvByAnalog.entity.RefreshToken;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.exceptions.*;
import com.gtr3base.AvByAnalog.repository.RefreshTokenRepository;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired";
    private static final String LOGIN_NOT_FOUND = "Username %s not found";

    public RefreshTokenService(JwtService jwtService, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String login){
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new LoginException(String.format(LOGIN_NOT_FOUND, login)));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken rToken = RefreshToken
                .builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(1, ChronoUnit.MINUTES))
                .build();

        return refreshTokenRepository.save(rToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),REFRESH_TOKEN_EXPIRED);
        }
        return token;
    }

    public AuthResponse processRefreshToken(RefreshTokenRequest refReq){
        return findByToken(refReq.token())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name());
                    return new AuthResponse(token, refReq.token());
                })
                .orElseThrow(() -> new TokenRefreshException(refReq.token(), REFRESH_TOKEN_EXPIRED));
    }
}
