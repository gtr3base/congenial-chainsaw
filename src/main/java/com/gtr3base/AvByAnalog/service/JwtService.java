package com.gtr3base.AvByAnalog.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private final Key key;
    private final long expirationMs;
    private final JwtParser jwtParser;
    private Integer userId;

    public JwtService(
            @Value("${AvByAnalog.jwt.secret}") String secret,
            @Value("${AvByAnalog.jwt.expires-in-min}") long expiresInMin
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expiresInMin * 60_000;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(Integer userId, String username, String role) {
        long now = System.currentTimeMillis();
        this.userId = userId;
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setClaims(Map.of("username", username, "role", role))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    public boolean isExpired(String token) {
        Date exp = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try{
            final Claims claims = jwtParser.parseClaimsJws(token).getBody();
            return claimsResolver.apply(claims);
        }catch (Exception e){
            log.error("Error extracting claim from token", e);
            return null;
        }
    }
}
