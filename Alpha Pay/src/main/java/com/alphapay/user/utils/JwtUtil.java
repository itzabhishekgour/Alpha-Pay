package com.alphapay.user.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {


    private static final String SECRET = "whKw!XzBaGREM1WD._?Kt>*iR+wLj0z8f@*R]Jz=1Q;";
    private final SecretKey secretKey =
        Keys.hmacShaKeyFor(SECRET.getBytes(java.nio.charset.StandardCharsets.UTF_8));


    private final long EXPIRATION_TIME = 3600000*24;

    public String generateToken(String email, Long userId, String role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(email)
                .claim("userId", String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }
}
