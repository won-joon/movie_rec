package com.movierec.demo;

import com.movierec.demo.auth.AuthSuccessHandler;
import com.movierec.demo.auth.jwt.JwtAuthenticationFilter;
import com.movierec.demo.auth.jwt.JwtProvider;
import com.movierec.demo.auth.LoginAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;

@RequiredArgsConstructor
public class TempSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtProvider jwtProvider;
    @Override
    public void configure(HttpSecurity builder) throws Exception {

        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(authenticationManager);

        loginAuthenticationFilter.setFilterProcessesUrl("/login");
        loginAuthenticationFilter.setAuthenticationSuccessHandler(new AuthSuccessHandler(jwtProvider));

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);

        // Spring Security Filter Chain에 추가
        builder.addFilter(loginAuthenticationFilter)
                .addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, LoginAuthenticationFilter.class);
    }
}
