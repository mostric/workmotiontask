package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;

public interface KafkaEmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto toInCheck(String id) throws EmployeeNotFoundException;

    EmployeeDto toApprove(String id) throws EmployeeNotFoundException;

    EmployeeDto toActivate(String id) throws EmployeeNotFoundException;

    EmployeeDto getStateForEmployeeId(String id) throws EmployeeNotFoundException;
}
