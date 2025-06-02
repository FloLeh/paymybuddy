package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionCreateRequest(
        @NotNull(message = "La transaction doit indiquer un destinataire non vide")
        Integer receiverId,

        String description,

        @Positive(message = "Le montant doit être supérieur à 0")
        Double amount
) {
}
