package com.workmotion.core.exceptions;

import lombok.Getter;

@Getter
public class EmployeeNotFoundException extends Throwable {

    public EmployeeNotFoundException(String id) {
        super(String.format("Employee with id %s not found.", id));
    }
}
