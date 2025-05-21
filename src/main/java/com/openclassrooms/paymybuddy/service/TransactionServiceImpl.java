package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionEntity> getTransactions() {
        return transactionRepository.findAll();
    }
}
