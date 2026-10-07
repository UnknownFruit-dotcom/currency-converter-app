package com.example.fruit.converter.exception.handler;

import com.example.fruit.converter.exception.ResourceNotFoundException;
import com.example.fruit.converter.exception.ProblemDetailsFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ProblemDetailsFactory.of(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Message JSON not readable: {}", ex.getMessage());
        return ProblemDetailsFactory.of(HttpStatus.BAD_REQUEST,
                "Message not readable.",
                request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = ProblemDetailsFactory.of(HttpStatus.BAD_REQUEST, "Validation failed", request);
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);
        return ProblemDetailsFactory.of(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error.",
                request);
    }
}