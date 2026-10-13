package com.avocadotech.assetsledger.company.exception;

import com.avocadotech.assetsledger.common.exception.ResourceNotFoundException;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
