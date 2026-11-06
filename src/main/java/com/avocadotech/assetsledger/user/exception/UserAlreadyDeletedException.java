package com.avocadotech.assetsledger.user.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

import java.util.UUID;

public class UserAlreadyDeletedException extends ConflictException {
    public UserAlreadyDeletedException(UUID id) {
        super("User " + id + " is already deleted.");
    }
}
