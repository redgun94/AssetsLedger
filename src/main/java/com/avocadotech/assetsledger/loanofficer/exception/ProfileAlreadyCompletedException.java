package com.avocadotech.assetsledger.loanofficer.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

public class ProfileAlreadyCompletedException extends ConflictException {
    public ProfileAlreadyCompletedException(String message) {
        super(message);
    }
}
