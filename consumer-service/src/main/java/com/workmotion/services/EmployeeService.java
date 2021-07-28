package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto changeEmployeeState(EmployeeDto employeeDto) throws Exception, EmployeeNotFoundException;
}
