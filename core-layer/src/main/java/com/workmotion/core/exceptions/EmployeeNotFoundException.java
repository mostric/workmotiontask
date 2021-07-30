package com.workmotion.core.exceptions;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String id) {
        super(String.format("Employee with id %s not found.", id));
    }
}
