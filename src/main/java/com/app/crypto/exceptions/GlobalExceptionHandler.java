package com.app.crypto.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCsvFileException.class)
    public ResponseEntity<Object> handleDuplicatedCsvFile(DuplicateCsvFileException exception) {
        log.error("DuplicatedCsvFileException: {}", exception.getMessage());
        return ResponseEntity.status(CONFLICT).body(Map.of("message", exception.getMessage()));
    }


    @ExceptionHandler(IngestCryptoDataException.class)
    public ResponseEntity<Object> handleIngestCryptoData(IngestCryptoDataException exception) {
        log.error("IngestCryptoDataException: {}", exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        String message = "";
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            message = violation.getMessage();
            errors.put(fieldName, message);
        }
        log.error("ConstraintViolation: {}", message);
        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }
}
