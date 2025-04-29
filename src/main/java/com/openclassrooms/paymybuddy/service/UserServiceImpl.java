package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email = " + email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("{noop}" + user.getPassword())
                .build();
    }

    public void save(User user) throws RuntimeException {
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        userRepository.save(user);
    }
}
