package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;

import java.util.List;

public interface TransactionService {
    List<TransactionEntity> getTransactionsByUser(UserEntity user);
    void addTransaction(TransactionEntity transaction);
}
