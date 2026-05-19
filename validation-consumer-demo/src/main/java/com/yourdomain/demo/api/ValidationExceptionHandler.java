package com.yourdomain.demo.api;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldViolation(error.getField(), error.getDefaultMessage()))
                .toList();

        ValidationErrorResponse body = new ValidationErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                violations
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SimpleErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable root = ex.getMostSpecificCause();
        if (root instanceof SQLException sqlEx && "23505".equals(sqlEx.getSQLState())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleErrorResponse(
                    Instant.now().toString(),
                    HttpStatus.CONFLICT.value(),
                    "Duplicate key, dữ liệu đã tồn tại"
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleErrorResponse(
                Instant.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data integrity violation"
        ));
    }

    public record ValidationErrorResponse(
            String timestamp,
            int status,
            String error,
            List<FieldViolation> violations
    ) {
    }

    public record FieldViolation(String field, String message) {
    }

    public record SimpleErrorResponse(
            String timestamp,
            int status,
            String error
    ) {
    }
}
