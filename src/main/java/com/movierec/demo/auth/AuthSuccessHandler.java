package com.movierec.demo.auth;

import com.movierec.demo.auth.jwt.JwtProvider;
import com.movierec.demo.domain.UserTokens;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2User 로 캐스팅하여 인증된 사용자 정보 가져옴
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();

        String name = oAuth2User.getUsername();
        UserTokens token = jwtProvider.generateLoginToken(name);

        response.setHeader("Authorization", token.getAccessToken());

        // 쿠키를 저장하는 걸로 설정
        Cookie cookie = new Cookie("JWT", token.getAccessToken());
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        String targetUrl = "/";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
