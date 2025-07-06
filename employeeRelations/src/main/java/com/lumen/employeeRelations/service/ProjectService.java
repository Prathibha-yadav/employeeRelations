package com.lumen.employeeRelations.service;

import com.lumen.employeeRelations.dto.ProjectDTO;
import com.lumen.employeeRelations.exception.ApiException;
import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.Project;
import com.lumen.employeeRelations.repository.EmployeeRepository;
import com.lumen.employeeRelations.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public List<Project> addProjectsToEmployee(Long employeeId, List<ProjectDTO> projectDTOs) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + employeeId));

            List<Project> projects = projectDTOs.stream().map(dto -> {
                Project project = new Project();
                project.setProjectName(dto.getProjectName());
                project.setStartDate(dto.getStartDate());
                project.setEndDate(dto.getEndDate());
                project.setEmployee(employee);
                return project;
            }).collect(Collectors.toList());

            return projectRepository.saveAll(projects);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to add projects to employee: " + ex.getMessage());
        }
    }
// Java docs
    public ProjectDTO getProjectById(Long id) {
        try {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Project not found with ID: " + id));

            ProjectDTO dto = new ProjectDTO();
            dto.setProjectName(project.getProjectName());
            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());
            dto.setEmployeeId(project.getEmployee() != null ? project.getEmployee().getEmployeeId() : null);
            return dto;
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to get project by ID: " + ex.getMessage());
        }
    }

    public List<ProjectDTO> getProjectsWithNoEmployees() {
        try {
            List<Project> projects = projectRepository.findProjectsWithNoEmployees();
            return projects.stream().map(p -> {
                ProjectDTO dto = new ProjectDTO();
                dto.setProjectId(p.getProjectId());
                dto.setProjectName(p.getProjectName());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to get projects with no employees: " + ex.getMessage());
        }
    }
    public List<ProjectDTO> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return projects.stream().map(p -> {
                ProjectDTO dto = new ProjectDTO();
                dto.setProjectId(p.getProjectId());
                dto.setProjectName(p.getProjectName());
                dto.setStartDate(p.getStartDate());
                dto.setEndDate(p.getEndDate());
                if (p.getEmployee() != null) {
                    dto.setEmployeeId(p.getEmployee().getEmployeeId());
                    dto.setEmployeeName(p.getEmployee().getFirstName() + " " + p.getEmployee().getLastName());
                }
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to fetch all projects: " + ex.getMessage());
        }
    }
    @Transactional
    public void createUnassignedProject(ProjectDTO projectDTO) {
        try {
            Project project = new Project();
            project.setProjectName(projectDTO.getProjectName());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());
            project.setEmployee(null);  // Explicitly unassigned
            projectRepository.save(project);
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to create unassigned project: " + ex.getMessage());
        }
    }

    @Transactional
    public void assignProjectToEmployee(Long projectId, Long employeeId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ApiException(404, "Project not found with ID: " + projectId));
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + employeeId));

            project.setEmployee(employee);
            projectRepository.save(project);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to assign project to employee: " + ex.getMessage());
        }
    }
}