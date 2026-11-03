package com.avocadotech.assetsledger.client.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

public class ClientEmailAlreadyExistsException extends ConflictException {
    public ClientEmailAlreadyExistsException(String email) {
        super("A client with email " + email + " already exists");
    }
}
