package com.movierec.demo.auth.jwt;

import com.movierec.demo.auth.PrincipalService;
import com.movierec.demo.domain.UserTokens;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private String secretKey = "tokensecretkeytokensecretkeytokensecretkeytokensecretkeytokensecretkey";
    private final PrincipalService principalService;

    public JwtProvider(PrincipalService principalService) {
        this.principalService = principalService;
    }


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public UserTokens generateLoginToken(String subject) {
        String refreshToken = generateToken(subject);
        String accessToken = generateToken(subject);

        return new UserTokens(refreshToken, accessToken);
    }

    public String generateToken(String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(3).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = principalService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, List.of(new SimpleGrantedAuthority("USER")));
    }
}
