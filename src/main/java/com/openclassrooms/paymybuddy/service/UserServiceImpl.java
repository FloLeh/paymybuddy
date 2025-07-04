package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.*;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserEntity user) throws RuntimeException {
        Assert.notNull(user, "user must not be null");
        Assert.isNull(user.getId(), "user id must be null");
        Assert.isTrue(StringUtils.hasText(user.getEmail()), "email must not be empty");
        Assert.isTrue(StringUtils.hasText(user.getUsername()), "username must not be empty");
        Assert.isTrue(StringUtils.hasText(user.getPassword()), "password must not be empty");
        Assert.isTrue(user.getConnections().isEmpty(), "connections must be empty");

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

    public void updateUser(UserEntity user) throws RuntimeException {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(user.getId(), "user id must not be null");
        UserEntity checkUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        Assert.isTrue(checkUser.getUsername().equals(user.getUsername()), "username can't be changed");
        Assert.isTrue(checkUser.getEmail().equals(user.getEmail()), "email can't be changed");
        Assert.isTrue(StringUtils.hasText(user.getPassword()), "password must not be empty");

        userRepository.save(user);
    }

    public void updatePassword(UserEntity user, String password) throws RuntimeException {
        Assert.notNull(password, "Le mot de passe est null.");
        Assert.isTrue(StringUtils.hasText(password), "Le mot de passe est vide.");
        String encodedPassword = passwordEncoder.encode(password);

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
        Assert.isTrue(!connectionEmail.equals(currentUserEmail), "L'utilisateur ne peut pas être vous même.");
        UserEntity user = findByEmail(currentUserEmail);

        UserEntity connection = userRepository.findByEmail(connectionEmail).orElseThrow(UserNotFoundException::new);

        if (user.getConnections().contains(connection)) {
            throw new UserAlreadyConnectedException();
        }

        Set<UserEntity> connections = user.getConnections();
        connections.add(connection);
        user.setConnections(connections);
        userRepository.save(user);
    }
}
