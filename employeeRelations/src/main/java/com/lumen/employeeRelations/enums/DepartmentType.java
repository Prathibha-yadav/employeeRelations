package com.lumen.employeeRelations.enums;

public enum DepartmentType {
    SOFTWARE_DEVELOPMENT(1, "Software Development"),
    HR(2, "Human Resources"),
    FINANCE(3, "Finance");

    private final int id;
    private final String name;

    DepartmentType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public static DepartmentType fromId(int id) {
        for (DepartmentType type : values()) {
            if (type.id == id) return type;
        }
        throw new IllegalArgumentException("Invalid Department ID: " + id);
    }
}