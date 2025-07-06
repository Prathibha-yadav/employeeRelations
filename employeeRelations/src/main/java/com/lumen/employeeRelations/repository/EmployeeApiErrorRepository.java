package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.model.EmployeeApiError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeApiErrorRepository extends JpaRepository<EmployeeApiError, Long> {
}