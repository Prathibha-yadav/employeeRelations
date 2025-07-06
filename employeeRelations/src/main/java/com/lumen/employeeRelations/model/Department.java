package com.lumen.employeeRelations.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    private Long departmentId;

    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}