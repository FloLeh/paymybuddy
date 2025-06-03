package com.openclassrooms.paymybuddy.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Le username ne peut pas être nulle ou vide")
        String username,

        @NotBlank(message = "L'email ne peut pas etre null ou vide")
        @Email(message = "L'email n'est pas valide")
        String email,

        @NotBlank
        @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
        String password) {
}
