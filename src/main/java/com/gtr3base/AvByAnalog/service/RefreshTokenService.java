package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.entity.RefreshToken;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.repository.RefreshTokenRepository;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String login){
        User user;
        if(userRepository.findByLogin(login).isPresent()){
            user = userRepository.findByLogin(login).get();
        }else{
            return null;
        }
        RefreshToken rToken = RefreshToken
                .builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))
                .build();

        refreshTokenRepository.save(rToken);
        return rToken;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Token expired");
        }
        return token;
    }
}
