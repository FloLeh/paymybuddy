package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionEntity> getTransactionsByUser(UserEntity user) {
        return transactionRepository.findBySender(user);
    }

    public void addTransaction(TransactionEntity transaction) {
        transactionRepository.save(transaction);
    }
}
