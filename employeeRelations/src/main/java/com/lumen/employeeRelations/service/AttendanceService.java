package com.lumen.employeeRelations.service;

import com.lumen.employeeRelations.dto.EmployeeDTO;
import com.lumen.employeeRelations.model.Attendance;
import com.lumen.employeeRelations.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private EmployeeService employeeService;

    private final Set<LocalDate> HOLIDAYS = Set.of(
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 14),
            LocalDate.of(2025, 2, 26),
            LocalDate.of(2025, 5, 1),
            LocalDate.of(2025, 8, 15),
            LocalDate.of(2025, 8, 27),
            LocalDate.of(2025, 9, 5),
            LocalDate.of(2025, 10, 2),
            LocalDate.of(2025, 10, 22),
            LocalDate.of(2025, 12, 25)
    );

    @Scheduled(cron = "0 0 0 * * *") // Every midnight
    public void runDailyAttendanceJob() {
        log.info("Running daily attendance scheduler at {}", LocalDate.now());
        initializeTodayAttendanceRecords();
        markLPForAbsentEmployees();
    }

    private void initializeTodayAttendanceRecords() {
        LocalDate today = LocalDate.now();
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        for (EmployeeDTO emp : employees) {
            attendanceRepository.findByEmployeeIdAndDate(emp.getEmployeeId(), today)
                    .orElseGet(() -> attendanceRepository.save(Attendance.builder()
                            .employeeId(emp.getEmployeeId())
                            .date(today)
                            .status(" ") // Initially empty
                            .build()));
        }
    }

    private void markLPForAbsentEmployees() {
        LocalDate today = LocalDate.now();
        List<LocalDate> last5WorkingDays = getLastNWorkingDays(today.minusDays(1), 5); // Ordered newest → oldest

        LocalDate oldestDayToCheck = last5WorkingDays.get(4); // 5th oldest = day to mark LP if condition met

        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        for (EmployeeDTO emp : employees) {
            Long empId = emp.getEmployeeId();

            List<Attendance> records = attendanceRepository.findByEmployeeIdAndDateRange(
                    empId, oldestDayToCheck, last5WorkingDays.get(0)); // date range

            Map<LocalDate, String> dateToStatus = records.stream()
                    .collect(Collectors.toMap(Attendance::getDate, Attendance::getStatus));

            boolean allMissingOrBlank = last5WorkingDays.stream()
                    .allMatch(date -> !dateToStatus.containsKey(date) || dateToStatus.get(date).isBlank());

            if (allMissingOrBlank) {
                Attendance record = attendanceRepository.findByEmployeeIdAndDate(empId, oldestDayToCheck)
                        .orElse(Attendance.builder()
                                .employeeId(empId)
                                .date(oldestDayToCheck)
                                .build());
                record.setStatus("LP");
                attendanceRepository.save(record);
            }
        }
    }

    private List<LocalDate> getLastNWorkingDays(LocalDate startFrom, int count) {
        List<LocalDate> workingDays = new ArrayList<>();
        LocalDate date = startFrom;
        while (workingDays.size() < count) {
            if (!isWeekend(date) && !HOLIDAYS.contains(date)) {
                workingDays.add(date);
            }
            date = date.minusDays(1);
        }
        return workingDays;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public void updateAttendance(Long empId, LocalDate date, String status) {
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(empId, date)
                .orElse(Attendance.builder().employeeId(empId).date(date).build());
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
    }
}
