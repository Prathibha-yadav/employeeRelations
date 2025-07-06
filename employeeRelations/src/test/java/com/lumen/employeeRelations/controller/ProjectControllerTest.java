package com.lumen.employeeRelations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.employeeRelations.dto.ProjectDTO;
import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.Project;
import com.lumen.employeeRelations.repository.EmployeeRepository;
import com.lumen.employeeRelations.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private EmployeeRepository employeeRepository;


    @Test
    public void testAddProjectsToEmployee() throws Exception {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        ProjectDTO dto = new ProjectDTO();
        dto.setProjectName("Assigned Project");
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(projectRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/projects/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(content().string("Projects added to employee with ID: " + employeeId));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(projectRepository, times(1)).saveAll(any());
    }

    @Test
    public void testGetProjectById() throws Exception {
        Long projectId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(10L);

        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectName("Existing Project");
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setEmployee(employee);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectName").value("Existing Project"))
                .andExpect(jsonPath("$.employeeId").value(10L));

        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    public void testGetProjectsWithNoEmployees() throws Exception {
        Project project1 = new Project();
        project1.setProjectId(1L);
        project1.setProjectName("No Emp Project 1");

        Project project2 = new Project();
        project2.setProjectId(2L);
        project2.setProjectName("No Emp Project 2");

        when(projectRepository.findProjectsWithNoEmployees()).thenReturn(List.of(project1, project2));

        mockMvc.perform(get("/projects/no-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].projectName").value("No Emp Project 1"))
                .andExpect(jsonPath("$[1].projectName").value("No Emp Project 2"));

        verify(projectRepository, times(1)).findProjectsWithNoEmployees();
    }

//    @Test
//    public void testAddProjectsToEmployee_EmployeeNotFound() throws Exception {
//        Long employeeId = 99L;
//        ProjectDTO dto = new ProjectDTO();
//        dto.setProjectName("Missing Emp Project");
//        dto.setStartDate(new Date());
//        dto.setEndDate(new Date());
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/projects/" + employeeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(List.of(dto))))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + employeeId));
//
//        verify(employeeRepository, times(1)).findById(employeeId);
//        verify(projectRepository, never()).saveAll(any());
//    }
//
//    @Test
//    public void testGetProjectById_NotFound() throws Exception {
//        Long projectId = 404L;
//        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/projects/" + projectId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Project not found with ID: " + projectId));
//
//        verify(projectRepository, times(1)).findById(projectId);
//    }


}