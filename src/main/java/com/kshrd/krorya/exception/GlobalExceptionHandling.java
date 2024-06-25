package com.kshrd.krorya.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandling {
//    @ExceptionHandler(DataAccessException.class)
//    public ProblemDetail handleDataAccessException(DataAccessException e) {
//        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Database access error: " + e.getMessage());
//        problemDetail.setTitle("Service Unavailable");
//        problemDetail.setType(URI.create("http://localhost:8080/api/v1/service-unavailable"));
//        problemDetail.setProperty("timestamp", new Date());
//        return problemDetail;
//    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ProblemDetail handleCannotCreateTransactionException(CannotCreateTransactionException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Database connection error: " + e.getMessage());
        problemDetail.setTitle("Service Unavailable");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/service-unavailable"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }
    @ExceptionHandler(PasswordException.class)
    public ProblemDetail handlePasswordException(PasswordException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        problemDetail.setTitle("Password Not Match");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ProblemDetail handleCustomNotFoundException(CustomNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Not Found");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/not-found"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ProblemDetail handleCustomBadRequestException(CustomBadRequestException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ProblemDetail handleSearchNotFoundException(SearchNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/not-found"));
        problemDetail.setTitle("Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Input invalid");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", new Date());
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        HashMap<String, String> errors = new HashMap<>();
        for (var parameterError : e.getAllValidationResults()) {
            String parameterName = parameterError.getMethodParameter().getParameterName();
            for (var error : parameterError.getResolvableErrors()) {
                errors.put(parameterName, error.getDefaultMessage());
            }
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(DuplicatedException.class)
    public ProblemDetail handleDuplicatedException(DuplicatedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Conflict");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/conflict"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Conflict");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/conflict"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(CustomUnauthorizedException.class)
    public ProblemDetail handleCustomUnauthorizedException(CustomUnauthorizedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle("Unauthorized");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/unauthorized"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/bad-request"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbiddenException(ForbiddenException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setTitle("Forbidden");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/forbidden"));
        problemDetail.setProperty("timestamp", new Date());
        return problemDetail;
    }
}
