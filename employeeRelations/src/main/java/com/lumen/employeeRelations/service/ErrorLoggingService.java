package com.lumen.employeeRelations.service;

import com.lumen.employeeRelations.dto.EmployeeApiErrorDTO;
import com.lumen.employeeRelations.model.EmployeeApiError;
import com.lumen.employeeRelations.repository.EmployeeApiErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ErrorLoggingService {

    @Autowired
    private EmployeeApiErrorRepository errorRepository;

    // Method to log errors
    public void logError(int code, String message, String details) {
        EmployeeApiError error = EmployeeApiError.builder()
                .errorCode(code)
                .errorMessage(message)
                .errorDetails(details)
                .timestamp(LocalDateTime.now())
                .build();

        // Save the error entity and convert it to DTO for further use
        errorRepository.save(error);

        // Optionally, you can return a DTO or process it further if needed
        EmployeeApiErrorDTO errorDTO = EmployeeApiErrorDTO.builder()
                .id(error.getId())
                .errorCode(error.getErrorCode())
                .errorMessage(error.getErrorMessage())
                .errorDetails(error.getErrorDetails())
                .timestamp(error.getTimestamp())
                .build();

        // You can return or use this DTO if needed
    }
}