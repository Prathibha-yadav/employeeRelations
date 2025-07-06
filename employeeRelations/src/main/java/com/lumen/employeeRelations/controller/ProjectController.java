package com.lumen.employeeRelations.controller;

import com.lumen.employeeRelations.dto.ProjectDTO;
import com.lumen.employeeRelations.service.EmployeeService;
import com.lumen.employeeRelations.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = {
"http://localhost:5173",
"http://localhost:5174",
 "http://127.0.0.1:5175"
})

@Tag(name = "Project Controller", description = "APIs for managing projects")
public class ProjectController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/{employeeid}")
    @Operation(summary = "Assign projects to employee", description = "Assigns a list of projects to an employee")
    public ResponseEntity<String> addProjectsToEmployee(
            @Parameter(description = "Employee ID to assign the projects to") @PathVariable Long employeeid,
            @RequestBody List<ProjectDTO> projectDTOs) {
        projectService.addProjectsToEmployee(employeeid, projectDTOs);
        return ResponseEntity.ok("Projects added to employee with ID: " + employeeid);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Fetches a project by its ID")
    public ResponseEntity<ProjectDTO> getProjectById(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/no-employees")
    @Operation(summary = "Get unassigned projects", description = "Fetches all projects not assigned to any employee")
    public ResponseEntity<List<ProjectDTO>> getProjectsWithNoEmployees() {
        return ResponseEntity.ok(projectService.getProjectsWithNoEmployees());
    }
    @GetMapping
    @Operation(summary = "Get all projects", description = "Fetches all projects with assigned employee (if any)")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    @PostMapping
    @Operation(summary = "Create unassigned project", description = "Creates a project that is not yet assigned to any employee")
    public ResponseEntity<String> createUnassignedProject(@RequestBody ProjectDTO projectDTO) {
        projectService.createUnassignedProject(projectDTO);
        return ResponseEntity.ok("Unassigned project created successfully.");
    }
    @PutMapping("/{projectId}/assign/{employeeId}")
    @Operation(summary = "Assign project to employee", description = "Assigns an existing unassigned project to an employee")
    public ResponseEntity<String> assignProjectToEmployee(
            @Parameter(description = "Project ID") @PathVariable Long projectId,
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        projectService.assignProjectToEmployee(projectId, employeeId);
        return ResponseEntity.ok("Project assigned successfully.");
    }
}

