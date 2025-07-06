package com.lumen.employeeRelations.controller;

import com.lumen.employeeRelations.dto.AttendanceDTO;
import com.lumen.employeeRelations.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lumen.employeeRelations.repository.AttendanceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174",
        "http://127.0.0.1:5175"
})

@RequiredArgsConstructor
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PutMapping("/{empId}")
    public String updateAttendance(@PathVariable Long empId,
                                   @RequestParam String status,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        attendanceService.updateAttendance(empId, date, status.toUpperCase());
        return "Attendance updated for Employee ID: " + empId + " on " + date;
    }
    @GetMapping("/test/scheduler")
    public String testScheduler() {
        attendanceService.runDailyAttendanceJob();
        return "Scheduler executed.";
    }

    @GetMapping("/all")
    @Operation(summary = "Get all attendance records", description = "Returns all employee attendance entries")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
        List<AttendanceDTO> attendanceList = attendanceRepository.findAll()
                .stream()
                .map(a -> AttendanceDTO.builder()
                        .employeeId(a.getEmployeeId())
                        .date(a.getDate())
                        .status(a.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(attendanceList);
    }
}