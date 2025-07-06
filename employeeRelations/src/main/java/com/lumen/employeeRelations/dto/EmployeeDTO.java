package com.lumen.employeeRelations.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.util.*;


@Data
public class EmployeeDTO {

    private Long employeeId;
    @NotBlank(message = "First name is required")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    private Date hireDate;
    private String jobId;
    @NotBlank(message = "Salary is required")
    private BigDecimal salary;
    private String departmentName;
    private DepartmentDTO department; // Enum-based name
    private DesignationDTO designation; // Enum-based name
    @NotBlank(message = "Employee Details are required")
    private EmployeeDetailsDTO employeeDetails;
    private Long departmentId;
    private List<ProjectDTO> projects;
    private long managerId;

    //mapToDTO
    public EmployeeDTO(){};

    //getEmployeesByCityAndDepartment
    public EmployeeDTO(Long employeeId, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //getHighestSalaryEmployeesByDepartment
    public EmployeeDTO(Long employeeId, String firstName, String departmentName, BigDecimal salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.departmentName = departmentName;
        this.salary = salary;

    }
    // getEmployeesWithAboveAverageSalary
    public EmployeeDTO(Long employeeId, String firstName, String lastName, String email, BigDecimal salary, Long departmentId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.departmentId = departmentId;
    }

}