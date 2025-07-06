package com.lumen.employeeRelations.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DesignationTypeTest {

    @Test
    public void testGetId() {
        assertEquals(1, DesignationType.SOFTWARE_ENGINEER.getId());
        assertEquals(2, DesignationType.SENIOR_ENGINEER.getId());
        assertEquals(3, DesignationType.TEAM_LEAD.getId());
        assertEquals(4, DesignationType.MANAGER.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Software Engineer", DesignationType.SOFTWARE_ENGINEER.getName());
        assertEquals("Senior Engineer", DesignationType.SENIOR_ENGINEER.getName());
        assertEquals("Team Lead", DesignationType.TEAM_LEAD.getName());
        assertEquals("Manager", DesignationType.MANAGER.getName());
    }

    @Test
    public void testFromId_ValidId() {
        assertEquals(DesignationType.SOFTWARE_ENGINEER, DesignationType.fromId(1));
        assertEquals(DesignationType.SENIOR_ENGINEER, DesignationType.fromId(2));
        assertEquals(DesignationType.TEAM_LEAD, DesignationType.fromId(3));
        assertEquals(DesignationType.MANAGER, DesignationType.fromId(4));
    }

    @Test
    public void testFromId_InvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DesignationType.fromId(999);  // Invalid ID
        });
        assertEquals("Invalid Designation ID: 999", exception.getMessage());
    }
}