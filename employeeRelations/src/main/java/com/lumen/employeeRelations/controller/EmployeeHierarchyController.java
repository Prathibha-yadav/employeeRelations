package com.lumen.employeeRelations.controller;

import com.lumen.employeeRelations.dto.EmployeeHierarchyDTO;
import com.lumen.employeeRelations.service.EmployeeHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hierarchy")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174",
        "http://127.0.0.1:5175"
})
public class EmployeeHierarchyController {

    @Autowired
    private EmployeeHierarchyService employeeHierarchyService;

    @PostMapping
    public ResponseEntity<String> assignManager(@RequestBody EmployeeHierarchyDTO dto) {
        employeeHierarchyService.assignManager(dto);
        return ResponseEntity.ok("Manager assigned successfully!");
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeHierarchyDTO> getManagerInfo(@PathVariable Long employeeId) {
        EmployeeHierarchyDTO result = employeeHierarchyService.getManagerInfo(employeeId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<EmployeeHierarchyDTO>> getEmployeesUnderManager(@PathVariable Long managerId) {
        List<EmployeeHierarchyDTO> result = employeeHierarchyService.getEmployeesUnderManager(managerId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeHierarchyDTO>> getEmployeeHierarchy() {
        List<EmployeeHierarchyDTO> hierarchyList = employeeHierarchyService.getEmployeeHierarchy();
        return ResponseEntity.ok(hierarchyList);
    }
}
