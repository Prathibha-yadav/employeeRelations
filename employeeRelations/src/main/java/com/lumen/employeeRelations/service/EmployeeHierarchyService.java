package com.lumen.employeeRelations.service;

import com.lumen.employeeRelations.dto.EmployeeHierarchyDTO;
import com.lumen.employeeRelations.exception.ApiException;
import com.lumen.employeeRelations.model.Employee;
import com.lumen.employeeRelations.model.EmployeeHierarchy;
import com.lumen.employeeRelations.repository.EmployeeHierarchyRepository;
import com.lumen.employeeRelations.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeHierarchyService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeHierarchyRepository employeeHierarchyRepository;

    public void assignManager(EmployeeHierarchyDTO dto) {
        try {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + dto.getEmployeeId()));

            Employee manager = employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ApiException(404, "Manager not found with ID: " + dto.getManagerId()));

            // Set manager on employee
            employee.setManager(manager);
            employeeRepository.save(employee);

            // Save hierarchy
            EmployeeHierarchy hierarchy = new EmployeeHierarchy();
            hierarchy.setEmployee(employee);  // Fully managed entity
            hierarchy.setManager(manager);    // Fully managed entity
            employeeHierarchyRepository.save(hierarchy);

        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to assign manager: " + ex.getMessage());
        }
    }

    public EmployeeHierarchyDTO getManagerInfo(Long employeeId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ApiException(404, "Employee not found with ID: " + employeeId));

            Employee manager = employee.getManager();

            return new EmployeeHierarchyDTO(
                    employee.getEmployeeId(),
                    manager != null ? manager.getEmployeeId() : null,
                    employee.getFirstName(),
                    manager != null ? manager.getFirstName() : null,
                    manager != null && manager.getDesignation() != null ? manager.getDesignation().getDesignationName() : null
            );
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to retrieve manager info: " + ex.getMessage());
        }
    }

    public List<EmployeeHierarchyDTO> getEmployeesUnderManager(Long managerId) {
        try {
            List<Object[]> results = employeeHierarchyRepository.findEmployeesUnderManager(managerId);
            return results.stream().map(row -> {
                Long empId = ((Number) row[0]).longValue();
                String empFirstName = (String) row[1];
                String designationName = (String) row[3];
                String managerFirstName = (String) row[4];
                return new EmployeeHierarchyDTO(
                        empId,
                        managerId,
                        empFirstName,
                        managerFirstName,
                        designationName
                );
            }).toList();
        } catch (Exception ex) {
            throw new ApiException(500, "Failed to retrieve employees under manager: " + ex.getMessage());
        }
    }
    public List<EmployeeHierarchyDTO> getEmployeeHierarchy() {
        List<EmployeeHierarchy> hierarchyList = employeeHierarchyRepository.findAll();

        return hierarchyList.stream().map(h -> {
            Employee employee = h.getEmployee();
            Employee manager = h.getManager();

            EmployeeHierarchyDTO dto = new EmployeeHierarchyDTO();
            dto.setEmployeeId(employee.getEmployeeId());
            dto.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            dto.setDesignation(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : null);

            if (manager != null) {
                dto.setManagerId(manager.getEmployeeId());
                dto.setManagerName(manager.getFirstName() + " " + manager.getLastName());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}