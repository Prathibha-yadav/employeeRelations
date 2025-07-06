package com.lumen.employeeRelations.controller;

import com.lumen.employeeRelations.dto.EmployeeDTO;
import com.lumen.employeeRelations.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing employee-related operations.
 * Provides endpoints for creating, retrieving, filtering, and deleting employees.
 */
@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5175" })
@Tag(name = "Employee Controller", description = "APIs for managing employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new employee.
     *
     * @param employeeDTO the employee data to be added
     * @return a success message
     */
    @PostMapping
    @Operation(summary = "Create a new employee", description = "Adds a new employee to the system")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok("Employee added successfully !!");
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of all employees
     */
    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves an employee by ID.
     *
     * @param id the ID of the employee to retrieve
     * @return the employee details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Fetches full employee details by employee ID")
    public ResponseEntity<EmployeeDTO> getEmployee(
            @Parameter(description = "ID of the employee to fetch") @PathVariable Long id) {
        EmployeeDTO found = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(found);
    }

    /**
     * Filters employees by city and department.
     *
     * @param city       the city to filter by
     * @param department the department to filter by
     * @return a list of employees matching the criteria
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter employees", description = "Find employees by city and department")
    public ResponseEntity<List<EmployeeDTO>> findByCityAndDepartment(
            @Parameter(description = "City of the employees") @RequestParam String city,
            @Parameter(description = "Department of the employees") @RequestParam String department) {
        List<EmployeeDTO> filtered = employeeService.getEmployeesByCityAndDepartment(city, department);
        return ResponseEntity.ok(filtered);
    }

    /**
     * Retrieves employees with the highest salary in each department.
     *
     * @return a list of employees with the highest salary per department
     */
    @GetMapping("/highest-salary")
    @Operation(summary = "Get highest salary employees", description = "Fetches employees with the highest salary in each department")
    public ResponseEntity<List<EmployeeDTO>> getHighestSalaries() {
        return ResponseEntity.ok(employeeService.getHighestSalaryEmployeesByDepartment());
    }

    /**
     * Retrieves employees earning above the average salary.
     *
     * @return a list of employees with above-average salaries
     */
    @GetMapping("/above-average-salary")
    @Operation(summary = "Get employees above average salary", description = "Fetches employees earning above average salary")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesAboveAverageSalary() {
        return ResponseEntity.ok(employeeService.getEmployeesWithAboveAverageSalary());
    }

    /**
     * Deletes an employee by ID.
     *
     * @param id the ID of the employee to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee by ID", description = "Deletes an employee from the system by ID")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully !!");
    }
}
