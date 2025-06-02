package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;

import java.util.List;

public interface TransactionService {
    List<TransactionRelativeAmount> getTransactionsWithRelativeAmount(UserEntity user);
    TransactionEntity createTransaction(TransactionCreateRequest transactionCreateRequest, String currentUserEmail);
}
