package com.lumen.employeeRelations.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {
    private Long employeeId;
    private LocalDate date;
    private String status;
}