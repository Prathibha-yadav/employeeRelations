package com.lumen.employeeRelations.service;

import com.lumen.employeeRelations.dto.*;
import com.lumen.employeeRelations.enums.DepartmentType;
import com.lumen.employeeRelations.enums.DesignationType;
import com.lumen.employeeRelations.exception.ApiException;
import com.lumen.employeeRelations.model.*;
import com.lumen.employeeRelations.repository.*;
import com.lumen.employeeRelations.validation.EmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeHierarchyRepository employeeHierarchyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        try {
            EmployeeValidator.validate(employeeDTO);

            // Map Department
            String deptName = employeeDTO.getDepartment().getDepartmentName();
            DepartmentType deptEnum = Arrays.stream(DepartmentType.values())
                    .filter(d -> d.getName().equalsIgnoreCase(deptName))
                    .findFirst()
                    .orElseThrow(() -> new ApiException(400, "Invalid department: " + deptName));
            Department department = departmentRepository.findById((long) deptEnum.getId())
                    .orElseThrow(() -> new ApiException(404, "Department not found"));

            // Map Designation
            String desigName = employeeDTO.getDesignation().getDesignationName();
            DesignationType desigEnum = Arrays.stream(DesignationType.values())
                    .filter(d -> d.getName().equalsIgnoreCase(desigName))
                    .findFirst()
                    .orElseThrow(() -> new ApiException(400, "Invalid designation: " + desigName));
            Designation designation = designationRepository.findById((long) desigEnum.getId())
                    .orElseThrow(() -> new ApiException(404, "Designation not found"));

            // Create Employee
            Employee employee = new Employee();
            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhoneNumber(employeeDTO.getPhoneNumber());
            employee.setHireDate(employeeDTO.getHireDate());
            employee.setJobId(employeeDTO.getJobId());
            employee.setSalary(employeeDTO.getSalary());
            employee.setDepartment(department);
            employee.setDesignation(designation);
            Employee savedEmployee = employeeRepository.save(employee);

            // Save Address
            AddressDTO addressDTO = employeeDTO.getEmployeeDetails().getAddress();
            Address address = new Address();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setZipCode(addressDTO.getZipCode());
            Address savedAddress = addressRepository.save(address);

            // Save EmployeeDetails
            EmployeeDetails details = new EmployeeDetails();
            details.setEmployee(savedEmployee);
            details.setAddress(savedAddress);
            details.setDateOfBirth(employeeDTO.getEmployeeDetails().getDateOfBirth());
            details.setMaritalStatus(employeeDTO.getEmployeeDetails().getMaritalStatus());
            employeeDetailsRepository.save(details);

            // Save Projects
            if (employeeDTO.getProjects() != null && !employeeDTO.getProjects().isEmpty()) {
                List<Project> projects = employeeDTO.getProjects().stream().map(dto -> {
                    Project project = new Project();
                    project.setProjectName(dto.getProjectName());
                    project.setStartDate(dto.getStartDate());
                    project.setEndDate(dto.getEndDate());
                    project.setEmployee(savedEmployee);
                    return project;
                }).collect(Collectors.toList());
                projectRepository.saveAll(projects);
            }

            return savedEmployee;

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Failed to create employee: " + e.getMessage());
        }
    }

    public EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setHireDate(employee.getHireDate());
        dto.setJobId(employee.getJobId());
        dto.setSalary(employee.getSalary());

        // Map Department
        if (employee.getDepartment() != null) {
            DepartmentDTO deptDTO = new DepartmentDTO();
            deptDTO.setDepartmentId(employee.getDepartment().getDepartmentId());
            deptDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
            dto.setDepartment(deptDTO);
        }

        // Map Designation
        if (employee.getDesignation() != null) {
            DesignationDTO desigDTO = new DesignationDTO();
            desigDTO.setDesignationId(employee.getDesignation().getDesignationId());
            desigDTO.setDesignationName(employee.getDesignation().getDesignationName());
            dto.setDesignation(desigDTO);
        }

        // Map EmployeeDetails and Address
        if (employee.getEmployeeDetails() != null) {
            EmployeeDetails ed = employee.getEmployeeDetails();
            Address addr = ed.getAddress();
            AddressDTO addressDTO = null;
            if (addr != null) {
                addressDTO = new AddressDTO();
                addressDTO.setStreet(addr.getStreet());
                addressDTO.setCity(addr.getCity());
                addressDTO.setState(addr.getState());
                addressDTO.setZipCode(addr.getZipCode());
            }
            EmployeeDetailsDTO edDTO = new EmployeeDetailsDTO();
            edDTO.setDateOfBirth(ed.getDateOfBirth());
            edDTO.setMaritalStatus(ed.getMaritalStatus());
            edDTO.setAddress(addressDTO);
            dto.setEmployeeDetails(edDTO);
        }

        // Map Projects
        if (employee.getProjects() != null) {
            List<ProjectDTO> projects = employee.getProjects().stream().map(p -> {
                ProjectDTO pdto = new ProjectDTO();
                pdto.setProjectId(p.getProjectId());
                pdto.setEmployeeId(employee.getEmployeeId());
                pdto.setProjectName(p.getProjectName());
                pdto.setStartDate(p.getStartDate());
                pdto.setEndDate(p.getEndDate());
                return pdto;
            }).collect(Collectors.toList());
            dto.setProjects(projects);
        }

        if (employee.getManager() != null) {
            dto.setManagerId(employee.getManager().getEmployeeId());
        }

        return dto;
    }

    public List<EmployeeDTO> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees.stream().map(this::mapToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to fetch employees");
        }
    }

    public EmployeeDTO getEmployeeById(Long id) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + id));
            return mapToDTO(employee);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Failed to fetch employee by ID");
        }
    }

    public List<EmployeeDTO> getEmployeesByCityAndDepartment(String city, String departmentName) {
        try {
            return employeeRepository.findByCityAndDepartment(city, departmentName)
                    .stream()
                    .map(e -> new EmployeeDTO(
                            ((Number) e[0]).longValue(),
                            (String) e[1],
                            (String) e[2]
                    )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to fetch filtered employees");
        }
    }

    public List<EmployeeDTO> getHighestSalaryEmployeesByDepartment() {
        try {
            return employeeRepository.findHighestSalaryEmployeesByDepartment()
                    .stream()
                    .map(e -> new EmployeeDTO(
                            ((Number) e[0]).longValue(),
                            (String) e[1],
                            (String) e[2],
                            (BigDecimal) e[3]
                    )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to fetch highest salary employees");
        }
    }

    public List<EmployeeDTO> getEmployeesWithAboveAverageSalary() {
        try {
            return employeeRepository.findEmployeesWithAboveAverageSalary()
                    .stream()
                    .map(e -> new EmployeeDTO(
                            ((Number) e[0]).longValue(),
                            (String) e[1],
                            (String) e[2],
                            (String) e[3],
                            (BigDecimal) e[4],
                            (Long) e[5]
                    )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to fetch employees with above average salary");
        }
    }
    @Transactional
    public void deleteEmployee(Long id) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + id));

            // Step 2: Set manager to null for all subordinates in hierarchy
            List<EmployeeHierarchy> subordinates = employeeHierarchyRepository.findByManager_EmployeeId(id);
            for (EmployeeHierarchy entry : subordinates) {
                entry.setManager(null); // Set manager to null
            }
            employeeHierarchyRepository.saveAll(subordinates); // Persist the updates

            // Step 3: Remove manager reference from employees (if mapped in Employee table too)
            List<Employee> reportingEmployees = employeeRepository.findByManager_EmployeeId(id);
            for (Employee e : reportingEmployees) {
                e.setManager(null);
            }
            employeeRepository.saveAll(reportingEmployees);
            // Delete Projects
            projectRepository.deleteAllByEmployee(employee);
            employeeHierarchyRepository.deleteAllByEmployee(employee);
            // Delete EmployeeDetails (and Address if needed)
            if (employee.getEmployeeDetails() != null) {
                addressRepository.delete(employee.getEmployeeDetails().getAddress());
                employeeDetailsRepository.delete(employee.getEmployeeDetails());

            }

            // Delete the Employee
            employeeRepository.delete(employee);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Failed to delete employee: " + e.getMessage());
        }
    }
}
