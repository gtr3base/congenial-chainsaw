package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.AuthResponse;
import com.gtr3base.AvByAnalog.dto.LoginRequest;
import com.gtr3base.AvByAnalog.dto.RefreshTokenRequest;
import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.entity.RefreshToken;
import com.gtr3base.AvByAnalog.service.AuthService;
import com.gtr3base.AvByAnalog.service.JwtService;
import com.gtr3base.AvByAnalog.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse registerUser(@Valid @RequestBody RegisterRequest req){
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req){
        return authService.login(req);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refReq){
        return refreshTokenService.findByToken(refReq.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token =
                            jwtService.generateToken(user.getId(),user.getUsername(),user.getRole().name());
                    return new AuthResponse(token, refReq.token());
                }).orElseThrow(() -> new RuntimeException("Error while refreshing token"));
    }
}
