package com.lumen.employeeRelations.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDetailsDTO {
    private Date dateOfBirth;
    private String maritalStatus;
    private AddressDTO address;
}