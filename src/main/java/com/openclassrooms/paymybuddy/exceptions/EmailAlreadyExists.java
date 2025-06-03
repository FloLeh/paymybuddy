package com.openclassrooms.paymybuddy.exceptions;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String email) {
        super("Email already exists : " + email);
    }
}
