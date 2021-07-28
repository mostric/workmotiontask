package com.workmotion.services.impl;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Setter
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        EmployeeModel employee = populateEmployeeModel(employeeDto);
        employee.setEmployeeState(EmployeeState.ADDED);
        return populateEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto changeEmployeeState(EmployeeDto employeeDto) throws EmployeeNotFoundException {
        Optional<EmployeeModel> employeeOptional = employeeRepository.findById(employeeDto.getId());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException(employeeDto.getId());
        }
        EmployeeModel employee = employeeOptional.get();
        employee.setEmployeeState(employeeDto.getEmployeeState());
        return populateEmployeeDto(employeeRepository.save(employee));
    }

    private EmployeeModel populateEmployeeModel(EmployeeDto employeeDto) {
        EmployeeModel employee = new EmployeeModel();
        employee.setId(employeeDto.getId());
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setContractInformation(employeeDto.getContractInformation());
        employee.setEmployeeState(employeeDto.getEmployeeState());
        return employee;
    }

    private EmployeeDto populateEmployeeDto(EmployeeModel employeeModel) {
        EmployeeDto employee = new EmployeeDto();
        employee.setId(employeeModel.getId());
        employee.setName(employeeModel.getName());
        employee.setAge(employeeModel.getAge());
        employee.setContractInformation(employeeModel.getContractInformation());
        employee.setEmployeeState(employeeModel.getEmployeeState());
        return employee;
    }
}
