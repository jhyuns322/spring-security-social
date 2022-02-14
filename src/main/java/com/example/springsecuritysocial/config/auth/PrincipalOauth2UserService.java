package com.example.springsecuritysocial.config.auth;

import com.example.springsecuritysocial.domain.User;
import com.example.springsecuritysocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken : " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User.getAttributes : " + oAuth2User.getAttributes());

        // 자동 회원 가입
        String provider = userRequest.getClientRegistration().getClientName(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("temp_password");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userResult = userRepository.findByUsername(username); // username 조회
        User user = new User();
        if (userResult == null) {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRole(role);
            user.setProvider(provider);
            user.setProviderId(providerId);
            System.out.println("user.toString : " + user.toString());
            userRepository.save(user);
        } else {
            user = userResult;
            System.out.println("이미 가입한 회원입니다.");
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
