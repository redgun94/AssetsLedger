package com.avocadotech.assetsledger.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User not found with id:"+ id );
    }
}
