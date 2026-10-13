package com.avocadotech.assetsledger.user.exception;

import com.avocadotech.assetsledger.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(UUID id) {
        super("User not found with id:"+ id );
    }
}
