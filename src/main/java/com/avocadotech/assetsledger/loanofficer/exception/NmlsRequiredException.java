package com.avocadotech.assetsledger.loanofficer.exception;

import com.avocadotech.assetsledger.common.exception.BusinessRuleException;

public class NmlsRequiredException extends BusinessRuleException {
    public NmlsRequiredException(String message) {
        super(message);
    }
}
