package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.exceptions.BusinessException;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


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
        doNothing().when(userService).addConnection("user@example.com", "friend@example.com");

        // when / then
        mockMvc.perform(post("/connections")
                        .param("email", "friend@example.com")
                        .with(user("user@example.com"))
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
