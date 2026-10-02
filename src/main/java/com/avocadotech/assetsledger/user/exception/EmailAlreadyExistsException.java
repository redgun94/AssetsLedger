package com.avocadotech.assetsledger.user.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {

        super("The email already has been registered previously" + email);
    }
}
