package com.example.springsecuritysocial.controller;

import com.example.springsecuritysocial.config.auth.PrincipalDetails;
import com.example.springsecuritysocial.domain.User;
import com.example.springsecuritysocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    /* 로그인 정보
     *  username / password / email / role
     *    user   /   1234   / null  / ROLE_USER
     *    admin  /   1234   / null  / ROLE_ADMIN
     */
    @GetMapping("/admin/userCreate")
    public String userCreate() {
        // Create New User Into DB
        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("1234"));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "home";
    }

    @GetMapping("/loadUserInfo")
    @ResponseBody
    public void loadUserInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("== load user info ==");
        // 일반 로그인 유저 정보(세션) 로드 방법 1
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getUser.toString : " + principalDetails.getUser().toString());

        // 일반 로그인 유저 정보(세션) 로드 방법 2
        System.out.println("userDetails.getUser.toString : " + userDetails.getUser().toString());
    }

    @GetMapping("/loadOAuthUserInfo")
    @ResponseBody
    public void loadOAuthUserInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth2User) {
        System.out.println("== load social user info ==");
        // 소셜 로그인 유저 정보(세션) 로드 방법 1
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes : " + oAuth2User.getAttributes());

        // 소셜 로그인 유저 정보(세션) 로드 방법 2
        System.out.println("oauth2User.getAttributes : " + oauth2User.getAttributes());

        // 일반 로그인 유저와 소셜 로그인 유저의 데이터 로드 방식이 달라 하나의 객체를 통해 데이터 로드 필요.
        // 그 작업을 PrincipalDetails 에서 두 클래스(UserDetails, OAuth2User)를 implements 하여 처리.
    }
}
