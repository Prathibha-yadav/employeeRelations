package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    void deleteAllByEmployee(Employee employee);

    @Query(value = """
    SELECT p.*
    FROM Project p
    WHERE p.employee_id IS NULL
    """, nativeQuery = true)
    List<Project> findProjectsWithNoEmployees();
}