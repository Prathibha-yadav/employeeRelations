package com.lumen.employeeRelations.enums;

public enum DesignationType {
    SOFTWARE_ENGINEER(1, "Software Engineer"),
    SENIOR_ENGINEER(2, "Senior Engineer"),
    TEAM_LEAD(3, "Team Lead"),
    MANAGER(4, "Manager");

    private final int id;
    private final String name;

    DesignationType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public static DesignationType fromId(int id) {
        for (DesignationType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Designation ID: " + id);
    }
}