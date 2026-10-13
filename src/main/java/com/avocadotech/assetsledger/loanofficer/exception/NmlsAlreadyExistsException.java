package com.avocadotech.assetsledger.loanofficer.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

public class NmlsAlreadyExistsException extends ConflictException {
    public NmlsAlreadyExistsException(String message) {
        super(message);
    }
}
