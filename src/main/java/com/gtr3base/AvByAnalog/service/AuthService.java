package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.AuthResponse;
import com.gtr3base.AvByAnalog.dto.LoginRequest;
import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.entity.RefreshToken;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.exceptions.LoginException;
import com.gtr3base.AvByAnalog.mappers.UserFromRequestMapper;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String USER_ALREADY_EXISTS_MSG = "Login %s is already in use";
    private static final String INVALID_CREDS_MSG = "Invalid credentials";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserFromRequestMapper userFromRequestMapper;

    private String token;
    private RefreshToken refreshToken;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, UserFromRequestMapper userFromRequestMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.userFromRequestMapper = userFromRequestMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req){
        String username = req.username().trim();
        String email = req.email().trim();

        if(userRepository.existsByUsername(username)){
            throw new LoginException(String.format(USER_ALREADY_EXISTS_MSG,username));
        }
        if(userRepository.existsByEmail(email)){
            throw new LoginException(String.format(USER_ALREADY_EXISTS_MSG,username));
        }

        User user = convertToEntity(req);

        user.setPassword(passwordEncoder.encode(req.password()));

        userRepository.save(user);

        token = jwtService.generateToken(user.getId(),username,user.getRole().name());
        refreshToken = refreshTokenService.createRefreshToken(username);

        return new AuthResponse(token, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest req){
        var user = userRepository.findByLogin(req.login().trim())
                .orElseThrow(()->new BadCredentialsException(INVALID_CREDS_MSG));

        if(!passwordEncoder.matches(req.password(),user.getPassword())){
            throw new BadCredentialsException(INVALID_CREDS_MSG);
        }

        String loginToken = jwtService.generateToken(user.getId(),user.getUsername(),user.getRole().name());

        if(refreshToken == null){
            refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        }

        return new AuthResponse(
                !token.isEmpty() ? token : loginToken, refreshToken.getToken());
    }

    private User convertToEntity(RegisterRequest reqDTO){
        return userFromRequestMapper.toUser(reqDTO);
    }
}
