package com.example.springsecuritysocial.service;

import com.example.springsecuritysocial.config.auth.PrincipalDetails;
import com.example.springsecuritysocial.domain.User;
import com.example.springsecuritysocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("user not found from DB");
            return null;
        }
        return new PrincipalDetails(user);
    }
}
