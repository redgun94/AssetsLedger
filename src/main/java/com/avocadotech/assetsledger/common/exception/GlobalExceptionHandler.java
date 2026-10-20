package com.avocadotech.assetsledger.common.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException exec){
        return problem(HttpStatus.NOT_FOUND, "Resource not found", exec.getMessage());
    }
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException exec){
        return problem(HttpStatus.CONFLICT, "Conflict",exec.getMessage());
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException exec){
        return problem(HttpStatus.BAD_REQUEST," Business rule violated", exec.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exec){
        log.error("Unexpected error", exec);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error","An unexpected error occurred");
    }
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException exec){
        return problem(HttpStatus.UNAUTHORIZED,"Unauthorized","Invalid email or password.");
    }

    @ExceptionHandler(InvalidSessionException.class)
    public ProblemDetail handleInvalidSessionException(){
        return problem(HttpStatus.UNAUTHORIZED,"Suspended", "This account is currently suspended");
    }
    private ProblemDetail problem(HttpStatus status, String title, String detail){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        return pd;
    }
}
