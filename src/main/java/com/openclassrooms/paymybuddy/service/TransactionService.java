package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionEntity;

import java.util.List;

public interface TransactionService {
    List<TransactionEntity> getTransactions();
}
