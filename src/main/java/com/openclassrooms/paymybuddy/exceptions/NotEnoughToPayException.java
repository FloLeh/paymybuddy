package com.openclassrooms.paymybuddy.exceptions;

public class NotEnoughToPayException extends BusinessException {
    public NotEnoughToPayException() {
        super("Argent insuffisant pour effectuer cette transaction.");
    }
}
