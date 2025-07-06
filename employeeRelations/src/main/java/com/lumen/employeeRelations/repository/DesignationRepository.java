package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.dto.DepartmentDTO;
import com.lumen.employeeRelations.dto.DesignationDTO;
import com.lumen.employeeRelations.enums.DesignationType;
import com.lumen.employeeRelations.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    DesignationDTO findByDesignationName(String designationName);
}