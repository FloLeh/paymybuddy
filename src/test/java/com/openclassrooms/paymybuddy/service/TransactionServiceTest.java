package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.paymybuddy.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

        receiver = new UserEntity();
        receiver.setId(2);
        receiver.setUsername("receiverUser");
    }

    @Test
    void getTransactionsWithRelativeAmount_shouldReturnCorrectRelativeAmounts() {
        // given
        TransactionEntity sentTransaction = new TransactionEntity(user, receiver, "Lunch", 50.0);
        TransactionEntity receivedTransaction = new TransactionEntity(receiver, user, "Refund", 20.0);

        when(transactionRepository.findBySender(user)).thenReturn(List.of(sentTransaction));
        when(transactionRepository.findByReceiver(user)).thenReturn(List.of(receivedTransaction));

        // when
        List<TransactionRelativeAmount> result = transactionService.getTransactionsWithRelativeAmount(user);

        // then
        assertEquals(2, result.size());

        TransactionRelativeAmount first = result.getFirst();
        assertEquals("receiverUser", first.relationName());
        assertEquals("Lunch", first.description());
        assertEquals(-50.0, first.amount());

        TransactionRelativeAmount second = result.get(1);
        assertEquals("receiverUser", second.relationName()); // sender of receivedTransaction
        assertEquals("Refund", second.description());
        assertEquals(20.0, second.amount());
    }

    @Test
    void createTransaction_shouldCreateAndSaveTransaction() {
        // given
        TransactionCreateRequest request = new TransactionCreateRequest(2, "Dinner", 75.0);
        String currentUserEmail = "sender@example.com";

        when(userService.findByEmail(currentUserEmail)).thenReturn(user);
        when(userService.findById(2)).thenReturn(receiver);

        ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TransactionEntity result = transactionService.createTransaction(request, currentUserEmail);

        // then
        verify(transactionRepository).save(captor.capture());
        TransactionEntity savedTransaction = captor.getValue();

        assertEquals(user, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals("Dinner", savedTransaction.getDescription());
        assertEquals(75.0, savedTransaction.getAmount());

        assertEquals(savedTransaction, result);
    }
}
