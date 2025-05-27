package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserEntity user) throws RuntimeException {
        Optional<UserEntity> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Optional<UserEntity> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow( () -> new RuntimeException("Email not found"));
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElseThrow( () -> new RuntimeException("User not found"));
    }

    public String addConnection(String userEmail, String connectionEmail) {
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow( () -> new RuntimeException("Email not found"));

        Optional<UserEntity> connection = userRepository.findByEmail(connectionEmail);
        if (connection.isEmpty()) {
            return "notFound";
        }

        if (user.getConnections().contains(connection.get())) {
            return "alreadyExists";
        }

        Set<UserEntity> connections = user.getConnections();
        connections.add(connection.get());
        user.setConnections(connections);
        userRepository.save(user);

        return "success";
    }
}
