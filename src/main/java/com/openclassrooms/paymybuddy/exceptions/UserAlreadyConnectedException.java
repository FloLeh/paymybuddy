package com.openclassrooms.paymybuddy.exceptions;

public class UserAlreadyConnectedException  extends BusinessException {
    public UserAlreadyConnectedException() {
        super("L'utilisateur est déjà présent dans vos relations.");
    }
}
