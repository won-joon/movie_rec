package com.movierec.demo.auth.oauth2;

import com.movierec.demo.auth.PrincipalDetails;
import com.movierec.demo.auth.domain.GoogleUserInfo;
import com.movierec.demo.auth.domain.OAuth2UserInfo;
import com.movierec.demo.domain.Role;
import com.movierec.demo.domain.User;
import com.movierec.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("패스워드");
        Role role = Role.USER;

        // 현재 정보 가져오기
        Optional<User> userEntity = userRepository.findByUsername(username);

        // DB 에 없는 사용자라면 회원 가입 처리
        if (userEntity.isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .role(role)
                    .build();

            userRepository.save(user);
            return new PrincipalDetails(user, oAuth2User.getAttributes());
        }else{
            return new PrincipalDetails(userEntity.get(), oAuth2User.getAttributes());
        }
    }
}
