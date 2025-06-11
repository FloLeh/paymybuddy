package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmountResponse;
import com.openclassrooms.paymybuddy.exceptions.NotEnoughToPayException;
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
    private final UserService userService;

    public List<TransactionRelativeAmountResponse> getTransactionsWithRelativeAmount(UserEntity user) {
        return transactionRepository.findAllByReceiverOrReceiver(user)
                .stream()
                .map(transactionEntity ->  new TransactionRelativeAmountResponse(transactionEntity, user))
                .toList();
    }

    public TransactionEntity createTransaction(TransactionCreateRequest transactionCreateRequest, UserEntity sender) {
        if (sender.getAccount() - transactionCreateRequest.amount() < 0 ) {
            throw new NotEnoughToPayException();
        }

        UserEntity receiver = userService.findById(transactionCreateRequest.receiverId());

        receiver.setAccount(receiver.getAccount() + transactionCreateRequest.amount());
        sender.setAccount(sender.getAccount() - transactionCreateRequest.amount());
        userService.updateUser(sender);
        userService.updateUser(receiver);

        TransactionEntity transaction = new TransactionEntity(sender, receiver, transactionCreateRequest.description(), transactionCreateRequest.amount());
        return transactionRepository.save(transaction);
    }
}
