package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmountResponse;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.paymybuddy.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private UserEntity user;
    private UserEntity receiver;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1);
        user.setUsername("senderUser");
        user.setAccount(BigDecimal.valueOf(100.0));

        receiver = new UserEntity();
        receiver.setId(2);
        receiver.setUsername("receiverUser");
        receiver.setAccount(BigDecimal.valueOf(100.0));
    }

    @Test
    void getTransactionsWithRelativeAmount_shouldReturnCorrectRelativeAmounts() {
        // given
        TransactionEntity sentTransaction = new TransactionEntity(user, receiver, "Lunch", BigDecimal.valueOf(50.0));
        sentTransaction.setId(1);
        TransactionEntity receivedTransaction = new TransactionEntity(receiver, user, "Refund", BigDecimal.valueOf(20.0));
        receivedTransaction.setId(2);

        when(transactionRepository.findAllBySenderOrReceiver(user)).thenReturn(List.of(sentTransaction, receivedTransaction));

        // when
        List<TransactionRelativeAmountResponse> result = transactionService.getTransactionsWithRelativeAmount(user);

        // then
        assertEquals(2, result.size());

        TransactionRelativeAmountResponse first = result.stream().filter(r -> r.description().equals("Lunch")).findFirst().orElseThrow();
        assertEquals("receiverUser", first.relationName());
        assertEquals(BigDecimal.valueOf(-50.0), first.amount());

        TransactionRelativeAmountResponse second = result.stream().filter(r -> r.description().equals("Refund")).findFirst().orElseThrow();
        assertEquals("receiverUser", second.relationName()); // relation is still the other user
        assertEquals(BigDecimal.valueOf(20.0), second.amount());
    }

    @Test
    void createTransaction_shouldCreateAndSaveTransaction() {
        // given
        TransactionCreateRequest request = new TransactionCreateRequest(2, "Dinner", BigDecimal.valueOf(75.0));
        String currentUserEmail = "sender@example.com";

        when(userService.findByEmail(currentUserEmail)).thenReturn(user);
        when(userService.findById(2)).thenReturn(receiver);

        ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TransactionEntity result = transactionService.createTransaction(request, user);

        // then
        verify(transactionRepository).save(captor.capture());
        TransactionEntity savedTransaction = captor.getValue();

        assertEquals(user, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals("Dinner", savedTransaction.getDescription());
        assertEquals(BigDecimal.valueOf(75.0), savedTransaction.getAmount());

        assertEquals(savedTransaction, result);
    }
}
