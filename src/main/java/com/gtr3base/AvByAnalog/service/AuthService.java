package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.AuthResponse;
import com.gtr3base.AvByAnalog.dto.LoginRequest;
import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.UserRole;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private String token = "";

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req){
        String username = req.username().trim();
        String email = req.email().trim().toLowerCase();

        if(userRepository.existsByUsername(username)){
            throw new DataIntegrityViolationException("Username is already in use");
        }
        if(userRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException("Email is already in use");
        }

        User user = User
                .builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(req.password()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        token = jwtService.generateToken(user.getId(),username,user.getRole().name());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req){
        var user = userRepository.findByLogin(req.login().trim())
                .orElseThrow(()->new BadCredentialsException("Invalid credentials"));

        if(!passwordEncoder.matches(req.password(),user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }

        String loginToken = jwtService.generateToken(user.getId(),user.getUsername(),user.getRole().name());
        return new AuthResponse(!token.isEmpty() ? token : loginToken);
    }
}
