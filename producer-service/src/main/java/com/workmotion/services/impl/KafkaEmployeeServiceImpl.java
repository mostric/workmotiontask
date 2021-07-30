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
    public EmployeeDto toInCheck(String id) throws EmployeeNotFoundException {
        return sendDataToEmployeeTopic(id, EmployeeState.IN_CHECK);
    }

    @Override
    public EmployeeDto toApprove(String id) throws EmployeeNotFoundException {
        return sendDataToEmployeeTopic(id, EmployeeState.APPROVED);
    }

    @Override
    public EmployeeDto toActivate(String id) throws EmployeeNotFoundException {
        return sendDataToEmployeeTopic(id, EmployeeState.ACTIVE);
    }

    @Override
    public EmployeeDto getStateForEmployeeId(String id) throws EmployeeNotFoundException {
        Optional<EmployeeModel> employeeModelOptional = employeeRepository.findById(id);
        if (employeeModelOptional.isEmpty()) {
            throw new EmployeeNotFoundException(id);
        }
        return getEmployeeDto(id, employeeModelOptional.get().getEmployeeState());
    }

    private EmployeeDto sendDataToEmployeeTopic(String id, EmployeeState state) throws EmployeeNotFoundException {
        if (employeeRepository.existsById(id)) {
            EmployeeDto employeeDto = getEmployeeDto(id, state);
            kafkaTemplate.send(employeeTopic, employeeDto);
            return employeeDto;
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
