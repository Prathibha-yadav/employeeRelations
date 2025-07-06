package com.lumen.employeeRelations.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DepartmentTypeTest {

    @Test
    public void testGetId() {
        assertEquals(1, DepartmentType.SOFTWARE_DEVELOPMENT.getId());
        assertEquals(2, DepartmentType.HR.getId());
        assertEquals(3, DepartmentType.FINANCE.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Software Development", DepartmentType.SOFTWARE_DEVELOPMENT.getName());
        assertEquals("Human Resources", DepartmentType.HR.getName());
        assertEquals("Finance", DepartmentType.FINANCE.getName());
    }

    @Test
    public void testFromId_ValidId() {
        assertEquals(DepartmentType.SOFTWARE_DEVELOPMENT, DepartmentType.fromId(1));
        assertEquals(DepartmentType.HR, DepartmentType.fromId(2));
        assertEquals(DepartmentType.FINANCE, DepartmentType.fromId(3));
    }

    @Test
    public void testFromId_InvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DepartmentType.fromId(999);  // Invalid ID
        });
        assertEquals("Invalid Department ID: 999", exception.getMessage());
    }
}