package com.avocadotech.assetsledger.loanofficer.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

import java.util.UUID;

public class ProfileAlreadyCompletedException extends ConflictException {
    public ProfileAlreadyCompletedException(UUID message) {
        super("User : "+message);
    }
}
