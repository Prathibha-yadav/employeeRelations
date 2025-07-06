package com.lumen.employeeRelations.repository;
import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.EmployeeHierarchy;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EmployeeHierarchyRepository extends JpaRepository<EmployeeHierarchy, Long> {
    List<EmployeeHierarchy> findByManager(Employee manager);
    List<EmployeeHierarchy> findByManager_EmployeeId(Long managerId);


    void deleteAllByEmployee(Employee employee);
    @Modifying
    @Transactional
    @Query(value = "UPDATE employee_hierarchy SET manager_id = NULL WHERE manager_id = :managerId", nativeQuery = true)
    void unsetManagerIdForSubordinates(@Param("managerId") Long managerId);


    @Query(value = """
    SELECT eh.employee_id, e.first_name, e.designation_id, d.designation_name, m.first_name as manager_first_name
    FROM employee_hierarchy eh
    JOIN employeetask3 e ON eh.employee_id = e.employee_id
    LEFT JOIN designation d ON e.designation_id = d.designation_id
    JOIN employeetask3 m ON eh.manager_id = m.employee_id
    WHERE eh.manager_id = :managerId
    """, nativeQuery = true)
    List<Object[]> findEmployeesUnderManager(@Param("managerId") Long managerId);
}