package com.avocadotech.assetsledger.loanofficer.exception;

import java.util.UUID;

public class LoanOfficerFoundException extends RuntimeException {
    public LoanOfficerFoundException(UUID id) {
        super("No record found" + id);
    }
}
