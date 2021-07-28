package com.workmotion.services;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.entities.EmployeeModel;

import java.util.Optional;

public interface KafkaEmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    void toInCheck(String id);

    void toApprove(String id);

    void toActivate(String id);

    Optional<EmployeeModel> getEmployeeById(String id);
}
