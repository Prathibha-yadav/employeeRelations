package com.lumen.employeeRelations.exception;

import com.lumen.employeeRelations.service.ErrorLoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorLoggingService errorLoggingService;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException ex) {
        errorLoggingService.logError(ex.getCode(), ex.getMessage(), "Handled ApiException");
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        errorLoggingService.logError(500, ex.getMessage(), "Unhandled Exception: " + ex.getClass().getName());
        return ResponseEntity.status(500).body("Internal Server Error: " + ex.getMessage());
    }
}