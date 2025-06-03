package com.openclassrooms.paymybuddy.exceptions;

public class EmailAlreadyExists extends BusinessException {
    public EmailAlreadyExists(String email) {
        super("Un compte existe déjà avec cet email : " + email);
    }
}
