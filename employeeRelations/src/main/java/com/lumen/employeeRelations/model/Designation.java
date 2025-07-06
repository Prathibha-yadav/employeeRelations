package com.lumen.employeeRelations.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "designation")
public class Designation {

    @Id
    private Long designationId;

    private String designationName;

    @OneToMany(mappedBy = "designation")
    private List<Employee> employees;
}