package com.movierec.demo;


import com.movierec.demo.auth.*;
import com.movierec.demo.auth.jwt.JwtAuthenticationFilter;
import com.movierec.demo.auth.jwt.JwtProvider;
import com.movierec.demo.auth.oauth2.PrincipalOauthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final PrincipalOauthUserService principalOauthUserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthSuccessHandler authSuccessHandler;
    private final CorsConfig corsConfig;
    private final JwtProvider jwtProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .addFilter(corsConfig.corsFilter())
                .sessionManagement((session) ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(((authorizeRequests) ->
                        authorizeRequests.anyRequest().permitAll()
                ))
                .formLogin((formLogin) ->
                    formLogin.disable()
                )
                .httpBasic((httpBasic) ->
                    httpBasic.disable()
                )
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .deleteCookies("JWT")
                )
                .oauth2Login((oauth) ->
                    oauth
                            .defaultSuccessUrl("/")
                            .userInfoEndpoint((userinfo) -> userinfo.userService(principalOauthUserService)) // 로그인 성공 이후 처리
                            .successHandler(authSuccessHandler)
                );

        return http
                .with(new TempSecurityConfig(jwtProvider), customizer -> {})
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public static BCryptPasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
