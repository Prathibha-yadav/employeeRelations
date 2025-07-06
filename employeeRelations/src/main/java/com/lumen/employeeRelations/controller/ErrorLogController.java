package com.lumen.employeeRelations.controller;

import com.lumen.employeeRelations.dto.EmployeeApiErrorDTO;
import com.lumen.employeeRelations.model.EmployeeApiError;
import com.lumen.employeeRelations.repository.EmployeeApiErrorRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/errors")
@CrossOrigin(origins = {
        "http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5175"
})
@RequiredArgsConstructor
public class ErrorLogController {

    @Autowired
    private EmployeeApiErrorRepository errorRepository;

    @GetMapping
    @Operation(
            summary = "Get all API error logs",
            description = "Retrieves a list of all error logs from the system"
    )
    public ResponseEntity<List<EmployeeApiErrorDTO>> getAllErrors() {
        List<EmployeeApiErrorDTO> errors = errorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(errors);
    }

    private EmployeeApiErrorDTO toDTO(EmployeeApiError error) {
        return EmployeeApiErrorDTO.builder()
                .id(error.getId())
                .errorCode(error.getErrorCode())
                .errorMessage(error.getErrorMessage())
                .errorDetails(error.getErrorDetails())
                .timestamp(error.getTimestamp())
                .build();
    }
}