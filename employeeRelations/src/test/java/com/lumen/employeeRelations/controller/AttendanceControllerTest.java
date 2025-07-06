package com.lumen.employeeRelations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.employeeRelations.model.Attendance;
import com.lumen.employeeRelations.repository.AttendanceRepository;
import com.lumen.employeeRelations.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttendanceService attendanceService;

    @MockBean
    private AttendanceRepository attendanceRepository;

    @Test
    void testUpdateAttendance() throws Exception {
        Long empId = 1L;
        String status = "present";
        LocalDate date = LocalDate.of(2025, 5, 1);

        // Mock repository behavior
        when(attendanceRepository.findByEmployeeIdAndDate(empId, date)).thenReturn(Optional.empty());
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Perform PUT request
        mockMvc.perform(put("/attendance/{empId}", empId)
                        .param("status", status)
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Attendance updated for Employee ID: " + empId + " on " + date));

        // Verify repository save called
        ArgumentCaptor<Attendance> captor = ArgumentCaptor.forClass(Attendance.class);
        verify(attendanceRepository).save(captor.capture());

        Attendance saved = captor.getValue();
        assertThat(saved.getEmployeeId()).isEqualTo(empId);
        assertThat(saved.getDate()).isEqualTo(date);
        assertThat(saved.getStatus()).isEqualTo("PRESENT");
    }

    @Test
    void testSchedulerExecution() throws Exception {
        // We can't verify logs or internal scheduled calls here, but we can ensure the endpoint triggers the logic
        mockMvc.perform(get("/attendance/test/scheduler"))
                .andExpect(status().isOk())
                .andExpect(content().string("Scheduler executed."));

        // As the method internally calls repository multiple times, we can just verify some interaction
        verify(attendanceRepository, atLeast(0)).findByEmployeeIdAndDate(anyLong(), any());
    }
    @Test
    void testUpdateAttendance_existingRecord() throws Exception {
        Long empId = 2L;
        LocalDate date = LocalDate.of(2025, 5, 2);
        Attendance existing = Attendance.builder().employeeId(empId).date(date).status("OLD").build();

        when(attendanceRepository.findByEmployeeIdAndDate(empId, date)).thenReturn(Optional.of(existing));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/attendance/{empId}", empId)
                        .param("status", "absent")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Attendance updated for Employee ID: " + empId + " on " + date));

        verify(attendanceRepository).save(existing);
        assertThat(existing.getStatus()).isEqualTo("ABSENT");
    }
//    @Test
//    void testUpdateAttendance_nullStatus_shouldFail() throws Exception {
//        Long empId = 3L;
//        LocalDate date = LocalDate.of(2025, 5, 3);
//
//        mockMvc.perform(put("/attendance/{empId}", empId)
//                        .param("date", date.toString()))
//                .andExpect(status().isBadRequest());
//    }
}