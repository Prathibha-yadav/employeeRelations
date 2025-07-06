package com.lumen.employeeRelations.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_hierarchy")
@Data
public class EmployeeHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
}