package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.dto.DepartmentDTO;
import com.lumen.employeeRelations.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
//    DepartmentDTO findByDepartmentName(String departmentName);
}
