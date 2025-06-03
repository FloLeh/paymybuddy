package com.openclassrooms.paymybuddy.exceptions;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String username) {
        super("Le username est déjà pris : " + username);
    }
}
