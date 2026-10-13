package com.avocadotech.assetsledger.loanofficer.exception;

import com.avocadotech.assetsledger.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class LoanOfficerNotFoundException extends ResourceNotFoundException {
    public LoanOfficerNotFoundException(UUID id) {
        super("No record found" + id);
    }
}
