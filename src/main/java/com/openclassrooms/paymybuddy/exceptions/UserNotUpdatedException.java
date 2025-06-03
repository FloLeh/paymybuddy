package com.openclassrooms.paymybuddy.exceptions;

public class UserNotUpdatedException extends BusinessException {
    public UserNotUpdatedException() {
        super("L'utilisateur n'a pas pu être modifié.");
    }
}
