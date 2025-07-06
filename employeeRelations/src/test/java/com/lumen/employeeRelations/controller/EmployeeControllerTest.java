package com.lumen.employeeRelations.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.employeeRelations.dto.*;
import com.lumen.employeeRelations.enums.DepartmentType;
import com.lumen.employeeRelations.enums.DesignationType;

import com.lumen.employeeRelations.model.*;
import com.lumen.employeeRelations.repository.*;
import com.lumen.employeeRelations.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private DesignationRepository designationRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private EmployeeDetailsRepository employeeDetailsRepository;

    @MockBean
    private ProjectRepository projectRepository;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setUp() {

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId((long) DepartmentType.SOFTWARE_DEVELOPMENT.getId());
        departmentDTO.setDepartmentName(DepartmentType.SOFTWARE_DEVELOPMENT.getName());

        // Prepare DesignationDTO
        DesignationDTO designationDTO = new DesignationDTO();
        designationDTO.setDesignationId((long) DesignationType.MANAGER.getId());
        designationDTO.setDesignationName(DesignationType.MANAGER.getName());

        // Prepare AddressDTO
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Hyderabad");
        addressDTO.setState("Telangana");
        addressDTO.setZipCode("500001");

        // Prepare EmployeeDetailsDTO
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
        employeeDetailsDTO.setDateOfBirth(new Date());
        employeeDetailsDTO.setMaritalStatus("Single");
        employeeDetailsDTO.setAddress(addressDTO);

        // Prepare ProjectDTO
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectName("Project Alpha");
        projectDTO.setStartDate(new Date());
        projectDTO.setEndDate(new Date());

        // Prepare EmployeeDTO
        employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setPhoneNumber("123-456-7890");
        employeeDTO.setHireDate(new Date());
        employeeDTO.setJobId("DEV123");
        employeeDTO.setSalary(new BigDecimal("75000"));
        employeeDTO.setDepartment(departmentDTO);
        employeeDTO.setDesignation(designationDTO);
        employeeDTO.setEmployeeDetails(employeeDetailsDTO);
        employeeDTO.setProjects(List.of(projectDTO));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        // Mocking the repository and save methods for creating an employee
        Department department = new Department();
        department.setDepartmentId((long) DepartmentType.SOFTWARE_DEVELOPMENT.getId());
        department.setDepartmentName(DepartmentType.SOFTWARE_DEVELOPMENT.getName());
        when(departmentRepository.findById((long) DepartmentType.SOFTWARE_DEVELOPMENT.getId()))
                .thenReturn(Optional.of(department));

        Designation designation = new Designation();
        designation.setDesignationId((long) DesignationType.MANAGER.getId());
        designation.setDesignationName(DesignationType.MANAGER.getName());
        when(designationRepository.findById((long) DesignationType.MANAGER.getId()))
                .thenReturn(Optional.of(designation));

        // Employee save mock
        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId(1L);
        savedEmployee.setFirstName(employeeDTO.getFirstName());
        savedEmployee.setLastName(employeeDTO.getLastName());
        savedEmployee.setEmail(employeeDTO.getEmail());
        savedEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        savedEmployee.setHireDate(employeeDTO.getHireDate());
        savedEmployee.setJobId(employeeDTO.getJobId());
        savedEmployee.setSalary(employeeDTO.getSalary());
        savedEmployee.setDepartment(department);
        savedEmployee.setDesignation(designation);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // Address save mock
        Address address = new Address();
        address.setAddressId(1L);
        address.setStreet(employeeDTO.getEmployeeDetails().getAddress().getStreet());
        address.setCity(employeeDTO.getEmployeeDetails().getAddress().getCity());
        address.setState(employeeDTO.getEmployeeDetails().getAddress().getState());
        address.setZipCode(employeeDTO.getEmployeeDetails().getAddress().getZipCode());
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // EmployeeDetails save mock
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setEmployee(savedEmployee);
        employeeDetails.setAddress(address);
        employeeDetails.setDateOfBirth(employeeDTO.getEmployeeDetails().getDateOfBirth());
        employeeDetails.setMaritalStatus(employeeDTO.getEmployeeDetails().getMaritalStatus());
        when(employeeDetailsRepository.save(any(EmployeeDetails.class))).thenReturn(employeeDetails);

        // Project save mock
        Project project = new Project();
        project.setProjectId(1L);
        project.setProjectName(employeeDTO.getProjects().get(0).getProjectName());
        project.setStartDate(employeeDTO.getProjects().get(0).getStartDate());
        project.setEndDate(employeeDTO.getProjects().get(0).getEndDate());
        project.setEmployee(savedEmployee);
        when(projectRepository.saveAll(any())).thenReturn(List.of(project));

        // Perform POST request
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee added successfully !!"));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testFilterEmployees_cityAndDepartment() throws Exception {
        List<Object[]> mockResult = List.of(
                new Object[]{1L, "Alice", "Brown"},
                new Object[]{2L, "Bob", "Green"}
        );

        Mockito.when(employeeRepository.findByCityAndDepartment("Chicago", "HR"))
                .thenReturn(mockResult);

        mockMvc.perform(get("/employees/filter")
                        .param("city", "Chicago")
                        .param("department", "HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("Bob"));
    }

    @Test
    void testGetHighestSalaries() throws Exception {
        // Creating mock data as Object[] for each row (representing one employee's data)
        Object[] row = new Object[]{
                1L,                          // employee_id
                "John",                       // first_name
                "Development",                // department_name
                new BigDecimal("90000")       // salary
        };

        // Mocking the repository call to return List<Object[]>
        Mockito.when(employeeRepository.findHighestSalaryEmployeesByDepartment())
                .thenReturn(List.<Object[]>of(row));  // Returning a List containing Object[]

        // Perform the mock MVC request to the controller
        mockMvc.perform(get("/employees/highest-salary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))  // Expect 1 employee
                .andExpect(jsonPath("$[0].employeeId").value(1))  // Expect employeeId 1
                .andExpect(jsonPath("$[0].firstName").value("John"))  // Expect firstName "John"
                .andExpect(jsonPath("$[0].departmentName").value("Development"))  // Expect department name "Development"
                .andExpect(jsonPath("$[0].salary").value(90000));  // Expect salary 90000
    }

    @Test
    void testGetEmployeesAboveAverageSalary() throws Exception {
        // Creating mock data as Object[] for each row (representing one employee's data)
        Object[] row = new Object[]{
                1L,                              // employee_id
                "John",                          // first_name
                "Doe",                           // last_name
                "john.doe@example.com",          // email
                new BigDecimal("80000"),         // salary
                1L                               // department_id
        };

        // Mocking the repository call to return List<Object[]>
        Mockito.when(employeeRepository.findEmployeesWithAboveAverageSalary())
                .thenReturn(List.<Object[]>of(row));  // Returning a List containing Object[]

        // Perform the mock MVC request to the controller
        mockMvc.perform(get("/employees/above-average-salary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))  // Expect 1 employee
                .andExpect(jsonPath("$[0].employeeId").value(1))  // Expect employeeId 1
                .andExpect(jsonPath("$[0].firstName").value("John"))  // Expect firstName "John"
                .andExpect(jsonPath("$[0].lastName").value("Doe"))  // Expect lastName "Doe"
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))  // Expect email
                .andExpect(jsonPath("$[0].salary").value(80000))  // Expect salary 80000
                .andExpect(jsonPath("$[0].departmentId").value(1L));  // Expect departmentId 1L
    }
    @Test
    public void testCreateEmployee_invalidDepartment() throws Exception {
        employeeDTO.getDepartment().setDepartmentName("InvalidDept");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }
//    @Test
//    void testCreateEmployee_invalidDepartmentId_throwsException() throws Exception {
//        employeeDTO.getDepartment().setDepartmentId(999L); // ID that doesn't exist
//        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/employees")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(employeeDTO)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Department not found"));
//    }
    @Test
    public void testCreateEmployee_departmentNotFound() throws Exception {
        when(departmentRepository.findById(any())).thenReturn(Optional.empty());
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateEmployee_missingFirstName_returnsBadRequest() throws Exception {
        employeeDTO.setFirstName(null);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("First name is required")));
    }

    @Test
    public void testCreateEmployee_invalidPhoneNumber_returnsBadRequest() throws Exception {
        employeeDTO.setPhoneNumber("14567");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Invalid phone number format (e.g., 555-123-4567)")));
    }

    @Test
    public void testCreateEmployee_invalidEmail_returnsBadRequest() throws Exception {
        employeeDTO.setEmail("john@");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Invalid email format")));
    }

    @Test
    public void testCreateEmployee_missingLastName_returnsBadRequest() throws Exception {
        employeeDTO.setLastName("");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Last name is required")));
    }

    @Test
    public void testCreateEmployee_missingHireDate_returnsBadRequest() throws Exception {
        employeeDTO.setHireDate(null);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Hire date is required")));
    }

    @Test
    public void testCreateEmployee_missingJobId_returnsBadRequest() throws Exception {
        employeeDTO.setJobId("");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Job ID is required")));
    }

    @Test
    public void testCreateEmployee_invalidSalary_returnsBadRequest() throws Exception {
        employeeDTO.setSalary(new BigDecimal("-5000"));
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Salary must be a non-negative value.")));
    }

    @Test
    public void testCreateEmployee_missingDateOfBirth_returnsBadRequest() throws Exception {
        employeeDTO.getEmployeeDetails().setDateOfBirth(null);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Date of birth is required")));
    }

    @Test
    public void testCreateEmployee_missingMaritalStatus_returnsBadRequest() throws Exception {
        employeeDTO.getEmployeeDetails().setMaritalStatus("");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Marital status is required")));
    }

    @Test
    public void testCreateEmployee_missingAddress_returnsBadRequest() throws Exception {
        employeeDTO.getEmployeeDetails().setAddress(null);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Address is required")));
    }

    @Test
    public void testMapToDTO_whenValidEmployee_shouldMapAllFields() {
        // Setup Department
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("Engineering");

        // Setup Designation
        Designation designation = new Designation();
        designation.setDesignationId(1L);
        designation.setDesignationName("Developer");

        // Setup Address
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Metropolis");
        address.setState("State");
        address.setZipCode("12345");

        // Setup EmployeeDetails
        EmployeeDetails details = new EmployeeDetails();
        java.sql.Date dob = java.sql.Date.valueOf(LocalDate.of(1990, 1, 1));
        details.setDateOfBirth(dob);
        details.setMaritalStatus("Single");
        details.setAddress(address);

        // Setup Projects
        java.sql.Date startDate = java.sql.Date.valueOf(LocalDate.of(1990, 1, 1));
        java.sql.Date endDate = java.sql.Date.valueOf(LocalDate.of(1990, 1, 1));

        Project project1 = new Project();
        project1.setProjectId(1L);
        project1.setProjectName("Project One");
        project1.setStartDate(startDate);
        project1.setEndDate(endDate);

        Project project2 = new Project();
        project2.setProjectId(2L);
        project2.setProjectName("Project Two");
        project2.setStartDate(startDate);
        project2.setEndDate(endDate);

        List<Project> projects = Arrays.asList(project1, project2);

        // Setup Manager
        Employee manager = new Employee();
        manager.setEmployeeId(99L);

        // Setup Employee
        Employee employee = new Employee();
        employee.setEmployeeId(10L);
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setEmail("alice.smith@example.com");
        employee.setPhoneNumber("9876543210");
        employee.setHireDate(dob);
        employee.setJobId("ENG001");
        employee.setSalary(new BigDecimal("90000"));
        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setEmployeeDetails(details);
        employee.setProjects(projects);
        employee.setManager(manager);

        // Call mapToDTO
        EmployeeDTO dto = employeeService.mapToDTO(employee);

        // Assertions
        assertEquals(10L, dto.getEmployeeId());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("alice.smith@example.com", dto.getEmail());
        assertEquals("9876543210", dto.getPhoneNumber());
        assertEquals("ENG001", dto.getJobId());
        assertEquals(new BigDecimal("90000"), dto.getSalary());

        assertEquals("Engineering", dto.getDepartment().getDepartmentName());
        assertEquals("Developer", dto.getDesignation().getDesignationName());

        assertEquals("123 Main St", dto.getEmployeeDetails().getAddress().getStreet());
        assertEquals("Metropolis", dto.getEmployeeDetails().getAddress().getCity());
        assertEquals("State", dto.getEmployeeDetails().getAddress().getState());
        assertEquals("12345", dto.getEmployeeDetails().getAddress().getZipCode());

        assertEquals("Single", dto.getEmployeeDetails().getMaritalStatus());
        assertEquals(dob, dto.getEmployeeDetails().getDateOfBirth()); // Use java.sql.Date for comparison

        assertEquals(2, dto.getProjects().size());
        assertEquals(startDate, dto.getProjects().get(0).getStartDate());
        assertEquals(endDate, dto.getProjects().get(0).getEndDate());

        assertEquals(dob, dto.getHireDate()); // Also compare as Date
        assertEquals(99L, dto.getManagerId());
    }

    @Test
    public void testMapToDTO_withoutEmployeeDetails() {
        Employee employee = new Employee();
        employee.setEmployeeId(2L);
        employee.setEmployeeDetails(null); // No employee details

        EmployeeDTO dto = employeeService.mapToDTO(employee);

        assertNull(dto.getEmployeeDetails());
    }
    @Test
    public void testMapToDTO_withoutProjects() {
        Employee employee = new Employee();
        employee.setEmployeeId(3L);
        employee.setProjects(null); // No projects

        EmployeeDTO dto = employeeService.mapToDTO(employee);

        assertNull(dto.getProjects());
    }
    @Test
    public void testMapToDTO_withoutManager() {
        Employee employee = new Employee();
        employee.setEmployeeId(4L);
        employee.setManager(null); // No manager

        EmployeeDTO dto = employeeService.mapToDTO(employee);

        assertEquals(0L,dto.getManagerId());
    }
    @Test
    public void testMapToDTO_withEmployeeDetailsButNullAddress() {
        EmployeeDetails details = new EmployeeDetails();
        details.setDateOfBirth(java.sql.Date.valueOf("1990-01-01"));
        details.setMaritalStatus("Single");
        details.setAddress(null); // Null address

        Employee employee = new Employee();
        employee.setEmployeeId(5L);
        employee.setEmployeeDetails(details);

        EmployeeDTO dto = employeeService.mapToDTO(employee);

        assertNotNull(dto.getEmployeeDetails());
        assertEquals("Single", dto.getEmployeeDetails().getMaritalStatus());
        assertNull(dto.getEmployeeDetails().getAddress());
    }

}
