package com.openclassrooms.paymybuddy.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
        @NotBlank(message = "Le username ne peut pas être nulle ou vide")
        String username,

        @NotBlank(message = "L'email ne peut pas etre null ou vide")
        @Email(message = "L'email n'est pas valide")
        String email,

        @NotBlank String password) {
}