package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(UserEntity user) throws RuntimeException {
        Optional<UserEntity> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Optional<UserEntity> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        log.info("Raw password {} - Encoded password: {}", user.getPassword(), encodedPassword);

        user.setPassword(encodedPassword);

        user = userRepository.save(user);

        log.info("Saved user: {}", user);
    }
}
