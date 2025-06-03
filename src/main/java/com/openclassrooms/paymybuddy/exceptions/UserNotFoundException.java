package com.openclassrooms.paymybuddy.exceptions;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("L'utilisateur n'a pas été trouvé.");
    }
}
