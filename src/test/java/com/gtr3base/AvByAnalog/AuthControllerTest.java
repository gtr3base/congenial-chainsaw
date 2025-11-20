package com.gtr3base.AvByAnalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtr3base.AvByAnalog.controller.AuthController;
import com.gtr3base.AvByAnalog.dto.AuthResponse;
import com.gtr3base.AvByAnalog.dto.LoginRequest;
import com.gtr3base.AvByAnalog.dto.RefreshTokenRequest;
import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.security.JwtAuthFilter;
import com.gtr3base.AvByAnalog.service.AuthService;
import com.gtr3base.AvByAnalog.service.JwtService;
import com.gtr3base.AvByAnalog.service.RefreshTokenService;
import com.gtr3base.AvByAnalog.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private RefreshTokenRequest refreshTokenRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("testuser", "test@example.com", "password123");
        loginRequest = new LoginRequest("testuser", "password123");
        authResponse = new AuthResponse("access-token-xyz", "refresh-token-xyz");
        refreshTokenRequest = new RefreshTokenRequest("refresh-token-xyz");
    }

    @Test
    void registerUser_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value(authResponse.accessToken()));
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        RegisterRequest invalidReq = new RegisterRequest("", "not-an-email", "pass");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValid() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authResponse.accessToken()));
    }

    @Test
    void refreshToken_ShouldReturnOk_WhenTokenIsValid() throws Exception {
        String newAccessToken = "new-jwt-token";
        AuthResponse refreshResponse = new AuthResponse(newAccessToken, refreshTokenRequest.token());

        when(refreshTokenService.processRefreshToken(any(RefreshTokenRequest.class)))
                .thenReturn(refreshResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshTokenRequest.token()));
    }

    @Test
    void refreshToken_ShouldThrowException_WhenServiceFails() {
        when(refreshTokenService.processRefreshToken(any(RefreshTokenRequest.class)))
                .thenThrow(new RuntimeException("Error while refreshing token"));

        Exception exception = assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(post("/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(refreshTokenRequest)));
        });

        Throwable rootCause = exception.getCause();
        assertTrue(rootCause instanceof RuntimeException);
        assertEquals("Error while refreshing token", rootCause.getMessage());
    }
}