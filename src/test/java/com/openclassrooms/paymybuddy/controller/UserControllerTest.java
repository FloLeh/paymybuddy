package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.exceptions.BusinessException;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setup() {
        user = new UserEntity();
        user.setUsername("TestUser");
        user.setEmail("user@example.com");
        user.setConnections(new HashSet<>());
        user.setAccount(100.0);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void profilePage_shouldDisplayUserData() throws Exception {
        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("active", "profile"))
                .andExpect(model().attribute("email", user.getEmail()))
                .andExpect(model().attribute("username", user.getUsername()));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void profilePageSubmit_shouldUpdatePasswordAndShowSuccess() throws Exception {
        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        mockMvc.perform(post("/profile")
                        .param("password", "newStrongPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("active", "profile"))
                .andExpect(model().attribute("email", user.getEmail()))
                .andExpect(model().attribute("username", user.getUsername()))
                .andExpect(model().attribute("errorMessage", ""));

        verify(userService).updatePassword(user, "newStrongPassword");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void profilePageSubmit_shouldHandlePasswordUpdateError() throws Exception {
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        doThrow(new RuntimeException("Mot de passe invalide")).when(userService).updatePassword(user, "bad");

        mockMvc.perform(post("/profile")
                        .param("password", "bad")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("active", "profile"))
                .andExpect(model().attribute("email", user.getEmail()))
                .andExpect(model().attribute("username", user.getUsername()))
                .andExpect(model().attribute("errorMessage", "Mot de passe invalide"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void connectionsPage_shouldReturnConnectionsView() throws Exception {
        // when / then
        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"))
                .andExpect(model().attribute("active", "connections"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void connectionsSubmit_shouldAddConnectionSuccessfully() throws Exception {
        // given
        doNothing().when(userService).addConnection(user.getEmail(), "friend@example.com");

        // when / then
        mockMvc.perform(post("/connections")
                        .param("email", "friend@example.com")
                        .with(user(user.getEmail()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("active", "connections"))
                .andExpect(model().attribute("errorMessage", ""));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void connectionsSubmit_shouldHandleBusinessException() throws Exception {
        // given
        doThrow(new BusinessException("Already connected"))
                .when(userService).addConnection("user@example.com", "friend@example.com");

        // when / then
        mockMvc.perform(post("/connections")
                        .param("email", "friend@example.com")
                        .with(user("user@example.com"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Already connected"));
    }
}
