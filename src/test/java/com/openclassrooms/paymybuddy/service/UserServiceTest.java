package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.*;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;
    private UserEntity connection;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1);
        user.setEmail("current@example.com");
        user.setUsername("currentUser");
        user.setPassword("plaintextPassword");
        user.setConnections(new HashSet<>());

        connection = new UserEntity();
        connection.setId(2);
        connection.setEmail("friend@example.com");
        connection.setUsername("friendUser");
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        // When
        userService.createUser(user);

        // Then
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_shouldThrowEmailAlreadyExists_whenEmailExists() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserEntity()));

        // When / Then
        assertThrows(EmailAlreadyExists.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowUsernameAlreadyExists_whenUsernameExists() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(new UserEntity()));

        // When / Then
        assertThrows(UsernameAlreadyExists.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    // -------------------- Tests findByEmail --------------------

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.findByEmail(user.getEmail());

        // Then
        assertEquals(user, result);
    }

    @Test
    void findByEmail_shouldThrowBusinessException_whenEmailNotFound() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(BusinessException.class, () -> userService.findByEmail(user.getEmail()));
    }

    // -------------------- Tests findById --------------------

    @Test
    void findById_shouldReturnUser_whenIdExists() {
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.findById(user.getId());

        // Then
        assertEquals(user, result);
    }

    @Test
    void findById_shouldThrowUserNotFoundException_whenIdNotFound() {
        // Given
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () -> userService.findById(999));
    }

    // -------------------- Tests addConnection --------------------

    @Test
    void addConnection_shouldAddConnectionSuccessfully() {
        // Given
        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(connection));

        // When
        userService.addConnection("current@example.com", "friend@example.com");

        // Then
        assertTrue(user.getConnections().contains(connection));
        verify(userRepository).save(user);
    }

    @Test
    void addConnection_shouldThrowUserNotFoundException_whenConnectionNotFound() {
        // Given
        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () ->
                userService.addConnection("current@example.com", "friend@example.com"));
    }

    @Test
    void addConnection_shouldThrowUserAlreadyConnectedException_whenAlreadyConnected() {
        // Given
        user.getConnections().add(connection);
        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(connection));

        // When / Then
        assertThrows(UserAlreadyConnectedException.class, () ->
                userService.addConnection("current@example.com", "friend@example.com"));
    }

    @Test
    void addConnection_shouldThrowUserAlreadyConnectedException_whenSelfConnection() {
        // Given
        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));

        // When / Then
        assertThrows(UserAlreadyConnectedException.class, () ->
                userService.addConnection("current@example.com", "current@example.com"));
    }
}
