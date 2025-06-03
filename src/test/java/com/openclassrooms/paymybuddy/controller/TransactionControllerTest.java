package com.openclassrooms.paymybuddy.controller;


import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private UserService userService;

    private UserEntity user;
    private List<TransactionRelativeAmount> transactions;

    @BeforeEach
    void setup() {
        user = new UserEntity();
        user.setEmail("user@example.com");
        user.setConnections(new HashSet<>());

        transactions = List.of(new TransactionRelativeAmount("friend", "desc", 100.0));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void transferPage_shouldDisplayTransactionsAndConnections() throws Exception {
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(transactionService.getTransactionsWithRelativeAmount(user)).thenReturn(transactions);

        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("active", "transfer"))
                .andExpect(model().attribute("connections", user.getConnections()))
                .andExpect(model().attribute("transactions", transactions));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void transfer_shouldCreateTransactionAndShowUpdatedData() throws Exception {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setSender(user);
        transaction.setAmount(50.0);
        transaction.setDescription("desc");

        List<TransactionRelativeAmount> updatedTransactions = List.of(
                new TransactionRelativeAmount("friend", "desc", -50.0)
        );

        when(transactionService.createTransaction(any(TransactionCreateRequest.class), eq("user@example.com")))
                .thenReturn(transaction);
        when(transactionService.getTransactionsWithRelativeAmount(user))
                .thenReturn(updatedTransactions);

        mockMvc.perform(post("/transfer")
                        .param("receiverId", "1")
                        .param("description", "desc")
                        .param("amount", "50.0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("active", "transfer"))
                .andExpect(model().attribute("connections", user.getConnections()))
                .andExpect(model().attribute("transactions", updatedTransactions));
    }
}