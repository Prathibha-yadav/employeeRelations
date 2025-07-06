package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.dto.EmployeeDetailsDTO;
import com.lumen.employeeRelations.model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {}
