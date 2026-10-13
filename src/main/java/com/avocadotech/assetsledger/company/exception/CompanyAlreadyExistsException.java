package com.avocadotech.assetsledger.company.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;

public class CompanyAlreadyExistsException extends ConflictException {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
