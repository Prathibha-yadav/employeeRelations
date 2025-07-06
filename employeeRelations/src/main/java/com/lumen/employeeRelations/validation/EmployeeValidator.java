package com.lumen.employeeRelations.validation;

import com.lumen.employeeRelations.dto.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

public class EmployeeValidator {

     static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );
     static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{3}-[0-9]{3}-[0-9]{4}$"
    );

    public static void validate(EmployeeDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Employee data is required");

        validateRequired(dto.getFirstName(), "First name");
        validateRequired(dto.getLastName(), "Last name");
        validateEmail(dto.getEmail());
        validatePhone(dto.getPhoneNumber());
        validateDate(dto.getHireDate(), "Hire date");
        validateRequired(dto.getJobId(), "Job ID");
        validateSalary(dto.getSalary());
        validateEmployeeDetails(dto.getEmployeeDetails());
    }


     static void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

     static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

     static void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format (e.g., 555-123-4567)");
        }
    }

     static void validateDate(Date date, String field) {
        if (date == null) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

     static void validateSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary must be a non-negative value.");
        }
    }

     static void validateEmployeeDetails(EmployeeDetailsDTO details) {

        if (details.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        validateRequired(details.getMaritalStatus(), "Marital status");

        AddressDTO addr = details.getAddress();
        if (addr == null) {
            throw new IllegalArgumentException("Address is required");
        }

        validateRequired(addr.getStreet(), "Street");
        validateRequired(addr.getCity(), "City");
        validateRequired(addr.getState(), "State");
        validateRequired(addr.getZipCode(), "Zip code");
    }

}