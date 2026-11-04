package com.avocadotech.assetsledger.common.exception;
import com.avocadotech.assetsledger.company.exception.CompanyAlreadyExistsException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors;
        errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "Invalid value"
                                : fieldError.getDefaultMessage(),
                        (first, second) -> first));

        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid.");
        pd.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(pd);
    }

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
        return problem(HttpStatus.BAD_REQUEST,"Business rule violated", exec.getMessage());
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

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public ProblemDetail handleCompanyAlreadyExists(CompanyAlreadyExistsException exec){
        ProblemDetail pd = problem(HttpStatus.CONFLICT, "Conflict", exec.getMessage());
        pd.setProperty("existingCompany", exec.getCompanyResponseDto());
        return pd;
    }
    private ProblemDetail problem(HttpStatus status, String title, String detail){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        return pd;
    }
}
