package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;

public interface KafkaEmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    void toInCheck(String id) throws EmployeeNotFoundException;

    void toApprove(String id) throws EmployeeNotFoundException;

    void toActivate(String id) throws EmployeeNotFoundException;

    EmployeeModel getEmployeeById(String id) throws EmployeeNotFoundException;
}
