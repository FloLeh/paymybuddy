package com.openclassrooms.paymybuddy.exceptions;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String username) {
        super("Username already exists : " + username);
    }
}
