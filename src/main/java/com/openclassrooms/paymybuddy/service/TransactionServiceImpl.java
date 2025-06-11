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
import org.springframework.util.Assert;

import java.math.BigDecimal;
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
        Assert.isTrue(transactionCreateRequest.amount().compareTo(BigDecimal.valueOf(0)) != 0, "Le montant ne peut être 0.");
        Assert.notNull(transactionCreateRequest.receiverId(), "Pas de relation sélectionnée.");

        BigDecimal amount = transactionCreateRequest.amount();
        BigDecimal senderAccount = sender.getAccount();

        if (senderAccount.compareTo(amount) < 0 ) {
            throw new NotEnoughToPayException();
        }

        UserEntity receiver = userService.findById(transactionCreateRequest.receiverId());

        sender.setAccount(senderAccount.subtract(amount));
        receiver.setAccount(receiver.getAccount().add(amount));

        userService.updateUser(sender);
        userService.updateUser(receiver);

        TransactionEntity transaction = new TransactionEntity(sender, receiver, transactionCreateRequest.description(), amount);
        return transactionRepository.save(transaction);
    }
}
