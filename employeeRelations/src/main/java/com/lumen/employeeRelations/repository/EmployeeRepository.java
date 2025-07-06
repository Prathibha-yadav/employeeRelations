package com.lumen.employeeRelations.repository;

import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.EmployeeHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByManager_EmployeeId(Long managerId);
    // Query to find employees by city and department
    @Query(value = """
        SELECT e.employee_id, e.first_name, e.last_name
        FROM employeetask3 e
        JOIN employee_details d ON e.employee_id = d.employee_id
        JOIN address a ON d.address_id = a.address_id
        JOIN department dep ON e.department_id = dep.department_id
        WHERE a.city = :city AND dep.department_name = :departmentName
    """, nativeQuery = true)
    List<Object[]> findByCityAndDepartment(@Param("city") String city, @Param("departmentName") String departmentName);

    // Query to find employees with the highest salary per department
    @Query(value = """
        SELECT e.employee_id, e.first_name, dep.department_name, e.salary
        FROM employeetask3 e
        JOIN department dep ON e.department_id = dep.department_id
        WHERE e.salary = (
            SELECT MAX(e.salary) FROM employeetask3 e WHERE e.department_id = dep.department_id
        )
    """, nativeQuery = true)
    List<Object[]> findHighestSalaryEmployeesByDepartment();

    // Query to find employees with salary above the average salary of all employees
    @Query(value = """
        SELECT e.employee_id, e.first_name, e.last_name, e.email, e.salary, e.department_id
        FROM employeetask3 e
        WHERE e.salary > (SELECT AVG(salary) FROM employeetask3)
    """, nativeQuery = true)
    List<Object[]> findEmployeesWithAboveAverageSalary();
}