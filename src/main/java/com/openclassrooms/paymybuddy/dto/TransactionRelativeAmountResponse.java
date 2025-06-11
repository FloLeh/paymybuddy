package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRelativeAmountResponse(
        @NotBlank
        String relationName,

        @NotBlank
        String description,

        @NotNull
        BigDecimal amount
) {

    public TransactionRelativeAmountResponse(TransactionEntity transactionEntity, UserEntity sender) {

        this(transactionEntity.getSender().equals(sender) ? transactionEntity.getReceiver().getUsername() : transactionEntity.getSender().getUsername(),
                transactionEntity.getDescription(),
                transactionEntity.getSender().equals(sender) ? transactionEntity.getAmount().negate() : transactionEntity.getAmount());
    }

}
