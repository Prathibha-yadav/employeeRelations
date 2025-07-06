package com.lumen.employeeRelations.dto;


import lombok.*;

import java.util.Date;

@Data
public class ProjectDTO {
    private String projectName;
    private Long projectId;
    private Date startDate;
    private Date endDate;
    private Long employeeId;
    private String employeeName;
}