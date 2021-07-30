package com.workmotion.services.impl;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Setter
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public EmployeeModel createEmployee(EmployeeDto employeeDto) {
        EmployeeModel employeeModel = populateEmployeeModel(employeeDto);
        return employeeRepository.save(employeeModel);
    }

    @Transactional
    @Override
    public EmployeeModel changeEmployeeState(EmployeeDto employeeDto) throws EmployeeNotFoundException {
        Optional<EmployeeModel> employeeModelOptional = employeeRepository.findById(employeeDto.getId());
        if (employeeModelOptional.isEmpty()) {
            throw new EmployeeNotFoundException(employeeDto.getId());
        }
        EmployeeModel employeeModel = employeeModelOptional.get();
        employeeModel.setEmployeeState(employeeDto.getEmployeeState());
        return employeeRepository.save(employeeModel);
    }

    private EmployeeModel populateEmployeeModel(EmployeeDto employeeDto) {
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setId(employeeDto.getId());
        employeeModel.setName(employeeDto.getName());
        employeeModel.setAge(employeeDto.getAge());
        employeeModel.setContractInformation(employeeDto.getContractInformation());
        employeeModel.setEmployeeState(employeeDto.getEmployeeState());
        return employeeModel;
    }
}
