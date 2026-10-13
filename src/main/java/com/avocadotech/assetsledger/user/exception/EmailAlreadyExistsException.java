package com.avocadotech.assetsledger.user.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String email) {

        super("The email already has been registered previously" + email);
    }
}
