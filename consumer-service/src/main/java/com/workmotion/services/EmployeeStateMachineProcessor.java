package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;

public interface EmployeeStateMachineProcessor {

    void process(EmployeeDto employeeDto);
}
