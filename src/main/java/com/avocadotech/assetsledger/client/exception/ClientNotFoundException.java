package com.avocadotech.assetsledger.client.exception;

import com.avocadotech.assetsledger.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class ClientNotFoundException extends ResourceNotFoundException {
    public ClientNotFoundException(UUID id) {
        super("No client found with id " + id);
    }
}
