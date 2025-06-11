package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "Le mot de passe ne doit pas être vide.")
        @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères.")
        String password
) {
}
