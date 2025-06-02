package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRelativeAmount(
        @NotBlank
        String relationName,

        @NotBlank
        String description,

        @NotNull
        Double amount
) {
}
