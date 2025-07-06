package com.lumen.employeeRelations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHierarchyDTO {
    private Long employeeId;
    private Long managerId;
    private String employeeName;
    private String managerName;
    private String designation;

}