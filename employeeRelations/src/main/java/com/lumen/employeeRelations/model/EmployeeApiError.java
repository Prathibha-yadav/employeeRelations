package com.lumen.employeeRelations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_api_error")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeApiError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "error_code", nullable = false)
    private int errorCode;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}