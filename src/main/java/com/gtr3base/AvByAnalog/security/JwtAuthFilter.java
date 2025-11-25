package com.gtr3base.AvByAnalog.security;

import com.gtr3base.AvByAnalog.repository.UserRepository;
import com.gtr3base.AvByAnalog.service.JwtService;
import com.gtr3base.AvByAnalog.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwt;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserDetailsServiceImpl userDetailsService, UserRepository users) {
        this.jwt = jwt;
        this.userDetailsService = userDetailsService;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        log.trace("JwtAuthFilter processing request: [METHOD: {}] [URI: {}]", request.getMethod(), request.getRequestURI());
        String token = header.substring(7);
        try {
            if (!jwt.isExpired(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwt.extractUsername(token);

                log.trace("Token valid. Extracted username: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                log.trace("Setting SecurityContext for user: {}", userDetails.getUsername());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("JWT Authentication error: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
