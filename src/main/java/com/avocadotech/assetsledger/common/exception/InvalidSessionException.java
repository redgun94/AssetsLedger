package com.avocadotech.assetsledger.common.exception;

import java.util.UUID;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException() {
        super("Invalid Session");
    }
}
