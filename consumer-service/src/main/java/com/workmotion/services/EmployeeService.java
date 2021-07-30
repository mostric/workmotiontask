package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;

public interface EmployeeService {

    EmployeeModel createEmployee(EmployeeDto employeeDto);

    EmployeeModel changeEmployeeState(EmployeeDto employeeDto) throws EmployeeNotFoundException;
}
