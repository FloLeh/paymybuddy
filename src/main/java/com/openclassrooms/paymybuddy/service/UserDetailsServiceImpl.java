package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email = " + email));
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
