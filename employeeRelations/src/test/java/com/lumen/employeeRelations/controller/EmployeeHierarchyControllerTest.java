package com.lumen.employeeRelations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.employeeRelations.dto.EmployeeHierarchyDTO;
import com.lumen.employeeRelations.model.Designation;
import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.EmployeeHierarchy;
import com.lumen.employeeRelations.repository.EmployeeHierarchyRepository;
import com.lumen.employeeRelations.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeHierarchyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmployeeHierarchyRepository employeeHierarchyRepository;

    @Test
    public void testAssignManager() throws Exception {
        Long employeeId = 1L;
        Long managerId = 2L;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John");

        Employee manager = new Employee();
        manager.setEmployeeId(managerId);
        manager.setFirstName("Manager");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeHierarchyRepository.save(any())).thenReturn(new EmployeeHierarchy());

        EmployeeHierarchyDTO dto = new EmployeeHierarchyDTO(employeeId, managerId, null, null, null);

        mockMvc.perform(post("/hierarchy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager assigned successfully!"));

        verify(employeeRepository, times(2)).findById(any());
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeHierarchyRepository, times(1)).save(any());
    }

    @Test
    public void testGetManagerInfo() throws Exception {
        Long employeeId = 1L;
        Long managerId = 2L;

        Employee manager = new Employee();
        manager.setEmployeeId(managerId);
        manager.setFirstName("Manager");
        Designation designation = new Designation();
        designation.setDesignationName("Team Lead");
        manager.setDesignation(designation);

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("Employee");
        employee.setManager(manager);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/hierarchy/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.managerId").value(managerId))
                .andExpect(jsonPath("$.employeeName").value("Employee"))
                .andExpect(jsonPath("$.managerName").value("Manager"))
                .andExpect(jsonPath("$.designation").value("Team Lead"));

        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testGetEmployeesUnderManager() throws Exception {
        Long managerId = 2L;

        List<Object[]> mockResults = List.of(
                new Object[]{1L, "Alice", 101L, "Developer", "Bob"},
                new Object[]{2L, "Charlie", 102L, "QA", "Bob"}
        );

        when(employeeHierarchyRepository.findEmployeesUnderManager(managerId)).thenReturn(mockResults);

        mockMvc.perform(get("/hierarchy/manager/" + managerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].employeeName").value("Alice"))
                .andExpect(jsonPath("$[0].managerName").value("Bob"))
                .andExpect(jsonPath("$[1].employeeName").value("Charlie"))
                .andExpect(jsonPath("$[1].designation").value("QA"));

        verify(employeeHierarchyRepository, times(1)).findEmployeesUnderManager(managerId);
    }

//    @Test
//    public void testAssignManager_EmployeeNotFound() throws Exception {
//        Long employeeId = 1L;
//        Long managerId = 2L;
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
//
//        EmployeeHierarchyDTO dto = new EmployeeHierarchyDTO(employeeId, managerId, null, null, null);
//
//        mockMvc.perform(post("/hierarchy")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + employeeId));
//
//        verify(employeeRepository, times(1)).findById(employeeId);
//    }

//    @Test
//    public void testAssignManager_ManagerNotFound() throws Exception {
//        Long employeeId = 1L;
//        Long managerId = 2L;
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(employeeId);
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
//        when(employeeRepository.findById(managerId)).thenReturn(Optional.empty());
//
//        EmployeeHierarchyDTO dto = new EmployeeHierarchyDTO(employeeId, managerId, null, null, null);
//
//        mockMvc.perform(post("/hierarchy")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Manager not found with ID: " + managerId));
//
//        verify(employeeRepository, times(2)).findById(anyLong());
//    }

//    @Test
//    public void testGetManagerInfo_EmployeeNotFound() throws Exception {
//        Long employeeId = 1L;
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/hierarchy/" + employeeId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + employeeId));
//
//        verify(employeeRepository, times(1)).findById(employeeId);
//    }
}