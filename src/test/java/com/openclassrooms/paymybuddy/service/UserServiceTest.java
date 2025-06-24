package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.*;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    private UserEntity newUser(){
        return new UserEntity("currentUser", "current@example.com", "plaintextPassword");
    }

    private UserEntity user1(){
        final UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail("current@example.com");
        user.setUsername("currentUser");
        user.setPassword("plaintextPassword");

        return user;
    }

    private UserEntity user2(){
        final UserEntity user = new UserEntity();
        user.setId(2);
        user.setEmail("friend@example.com");
        user.setUsername("friendUser");
        user.setPassword("plaintextPassword");
        return user;
    }

    // --- updatePassword ---

    @Test
    void givenValidPassword_whenUpdatePassword_thenPasswordEncodedAndUserSaved() {
        // given
        final UserEntity user = user1();
        String rawPassword = "newPassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // when
        userService.updatePassword(user, rawPassword);

        // then
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void givenBlankPassword_whenUpdatePassword_thenThrowsException() {
        // given
        final UserEntity user = user1();
        String blankPassword = " ";

        // when / then
        assertThatThrownBy(() -> userService.updatePassword(user, blankPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Le mot de passe est vide");
    }

    // --- updateUser ---

    @Test
    void givenValidUser_whenUpdateUser_thenUserSaved() {
        // given
        final UserEntity user = user1();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // when
        userService.updateUser(user);

        // then
        verify(userRepository).save(user);
    }

    @Test
    void givenNullUser_whenUpdateUser_thenThrowsException() {
        // given
        UserEntity nullUser = null;

        // when / then
        assertThatThrownBy(() -> userService.updateUser(nullUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("user must not be null");
    }

    @Test
    void givenUserWithNullId_whenUpdateUser_thenThrowsException() {
        // given
        final UserEntity user = user1();
        user.setId(null);

        // when / then
        assertThatThrownBy(() -> userService.updateUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("user id must not be null");
    }

    @Test
    void givenUserNotFound_whenUpdateUser_thenThrowsUserNotFoundException() {
        // given
        final UserEntity user = user1();
        log.info(user.toString());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> userService.updateUser(user))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void givenChangedUsername_whenUpdateUser_thenThrowsException() {
        // given
        final UserEntity user = user1();
        UserEntity dbUser = new UserEntity();
        dbUser.setId(1);
        dbUser.setUsername("original");
        dbUser.setEmail("user@example.com");
        dbUser.setPassword("password");

        when(userRepository.findById(1)).thenReturn(Optional.of(dbUser));
        user.setUsername("changed");

        // when / then
        assertThatThrownBy(() -> userService.updateUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username can't be changed");
    }

    @Test
    void givenChangedEmail_whenUpdateUser_thenThrowsException() {
        // given
        final UserEntity user = user1();
        UserEntity dbUser = new UserEntity();
        dbUser.setId(1);
        dbUser.setUsername(user.getUsername());
        dbUser.setEmail("original@example.com");
        dbUser.setPassword("password");

        when(userRepository.findById(1)).thenReturn(Optional.of(dbUser));
        user.setEmail("changed@example.com");

        // when / then
        assertThatThrownBy(() -> userService.updateUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email can't be changed");
    }

    @Test
    void givenEmptyPassword_whenUpdateUser_thenThrowsException() {
        // given
        final UserEntity user = user1();
        user.setPassword(" ");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // when / then
        assertThatThrownBy(() -> userService.updateUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("password must not be empty");
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {
        // Given a new user
        final UserEntity user = newUser();

        final String encodedPassword = "encodedPassword";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);

        // When
        userService.createUser(user);

        // Then
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_shouldThrowEmailAlreadyExists_whenEmailExists() {
        // Given
        final UserEntity user = newUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserEntity()));

        // When / Then
        assertThrows(EmailAlreadyExists.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowUsernameAlreadyExists_whenUsernameExists() {
        // Given
        final UserEntity user = newUser();

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
        final UserEntity user = newUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.findByEmail(user.getEmail());

        // Then
        assertEquals(user, result);
    }

    @Test
    void findByEmail_shouldThrowBusinessException_whenEmailNotFound() {
        // Given
        final UserEntity user = newUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(BusinessException.class, () -> userService.findByEmail(user.getEmail()));
    }

    // -------------------- Tests findById --------------------

    @Test
    void findById_shouldReturnUser_whenIdExists() {
        // Given
        final UserEntity user = newUser();
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
        // Given 2 users
        final UserEntity user = user1();
        final UserEntity connection = user2();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(connection.getEmail())).thenReturn(Optional.of(connection));

        // When
        userService.addConnection(user.getEmail(), connection.getEmail());

        // Then
        assertTrue(user.getConnections().contains(connection));
        verify(userRepository).save(user);
    }

    @Test
    void addConnection_shouldThrowUserNotFoundException_whenConnectionNotFound() {
        // Given
        final UserEntity user = user1();

        final String userNotExistsEmail = "user_not_exists@example.com";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userNotExistsEmail)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () ->
                userService.addConnection(user.getEmail(), userNotExistsEmail));
    }

    @Test
    void addConnection_shouldThrowUserAlreadyConnectedException_whenAlreadyConnected() {
        // Given
        final UserEntity user = user1();
        final UserEntity connection = user2();
        user.getConnections().add(connection);

        when(userRepository.findByEmail("current@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(connection));

        // When / Then
        assertThrows(UserAlreadyConnectedException.class, () ->
                userService.addConnection("current@example.com", "friend@example.com"));
    }
}
