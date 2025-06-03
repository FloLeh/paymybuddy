package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.exceptions.NotEnoughToPayException;
import com.openclassrooms.paymybuddy.exceptions.UserNotUpdatedException;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public List<TransactionRelativeAmount> getTransactionsWithRelativeAmount(UserEntity user) {
        List<TransactionEntity> transactionsAsSender = transactionRepository.findBySender(user);
        List<TransactionEntity> transactionsAsReceiver = transactionRepository.findByReceiver(user);
        return Stream.concat(transactionsAsSender.stream(), transactionsAsReceiver.stream())
                .sorted(Comparator.comparing(TransactionEntity::getId))
                .map(transactionEntity -> {
            boolean isSender = transactionEntity.getSender().equals(user);
            return new TransactionRelativeAmount(
                    isSender ? transactionEntity.getReceiver().getUsername() : transactionEntity.getSender().getUsername(),
                    transactionEntity.getDescription(),
                    isSender ? - transactionEntity.getAmount() : transactionEntity.getAmount()
            );
        }).toList();
    }

    public TransactionEntity createTransaction(TransactionCreateRequest transactionCreateRequest, UserEntity sender) {
        if (sender.getAccount() < transactionCreateRequest.amount()) {
            throw new NotEnoughToPayException();
        }

        UserEntity receiver = userService.findById(transactionCreateRequest.receiverId());
        try {
            receiver.setAccount(receiver.getAccount() + transactionCreateRequest.amount());
            sender.setAccount(sender.getAccount() - transactionCreateRequest.amount());
            userService.updateUser(sender);
            userService.updateUser(receiver);
        } catch (UserNotUpdatedException e) {
            throw new UserNotUpdatedException();
        }

        TransactionEntity transaction = new TransactionEntity(sender, receiver, transactionCreateRequest.description(), transactionCreateRequest.amount());
        return transactionRepository.save(transaction);
    }
}
