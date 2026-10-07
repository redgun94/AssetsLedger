package com.avocadotech.assetsledger.loanofficer.exception;

public class ProfileAlreadyCompletedException extends RuntimeException {
    public ProfileAlreadyCompletedException(String message) {
        super(message);
    }
}
