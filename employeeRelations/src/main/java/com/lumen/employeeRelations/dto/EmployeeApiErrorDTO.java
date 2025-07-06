package com.lumen.employeeRelations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeApiErrorDTO {

    private Long id;
    private int errorCode;
    private String errorMessage;
    private String errorDetails;
    private LocalDateTime timestamp;
}