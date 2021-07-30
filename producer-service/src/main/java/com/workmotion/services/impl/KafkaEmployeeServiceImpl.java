package com.workmotion.services.impl;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.services.KafkaEmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class KafkaEmployeeServiceImpl implements KafkaEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final KafkaTemplate<String, EmployeeDto> kafkaTemplate;
    private final String employeeTopic;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        String id = UUID.randomUUID().toString();
        employeeDto.setId(id);
        kafkaTemplate.send(employeeTopic, employeeDto);
        return employeeDto;
    }

    @Override
    public void toInCheck(String id) throws EmployeeNotFoundException {
        sendDataToEmployeeTopic(id, EmployeeState.IN_CHECK);
    }

    @Override
    public void toApprove(String id) throws EmployeeNotFoundException {
        sendDataToEmployeeTopic(id, EmployeeState.APPROVED);
    }

    @Override
    public void toActivate(String id) throws EmployeeNotFoundException {
        sendDataToEmployeeTopic(id, EmployeeState.ACTIVE);
    }

    @Override
    public EmployeeModel getEmployeeById(String id) throws EmployeeNotFoundException {
        Optional<EmployeeModel> employeeModelOptional = employeeRepository.findById(id);
        if (employeeModelOptional.isEmpty()) {
            throw new EmployeeNotFoundException(id);
        }
        return employeeModelOptional.get();
    }

    private void sendDataToEmployeeTopic(String id, EmployeeState state) throws EmployeeNotFoundException {
        if (employeeRepository.existsById(id)) {
            EmployeeDto employeeDto = getEmployeeDto(id, state);
            kafkaTemplate.send(employeeTopic, employeeDto);
        } else {
            throw new EmployeeNotFoundException(id);
        }
    }

    private EmployeeDto getEmployeeDto(String id, EmployeeState state) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(id);
        employeeDto.setEmployeeState(state);
        return employeeDto;
    }
}
