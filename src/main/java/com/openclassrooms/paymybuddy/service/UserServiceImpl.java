package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.*;
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
            throw new EmailAlreadyExists(user.getEmail());
        }

        Optional<UserEntity> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new UsernameAlreadyExists(user.getUsername());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow( () -> new BusinessException("Email not found"));
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void addConnection(String currentUserEmail, String connectionEmail) {
        UserEntity user = findByEmail(currentUserEmail);

        UserEntity connection = userRepository.findByEmail(connectionEmail).orElseThrow(UserNotFoundException::new);

        if (user.getConnections().contains(connection) || connectionEmail.equals(currentUserEmail)) {
            throw new UserAlreadyConnectedException();
        }

        Set<UserEntity> connections = user.getConnections();
        connections.add(connection);
        user.setConnections(connections);
        userRepository.save(user);
    }
}
