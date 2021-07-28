package com.workmotion.services.impl;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.entities.EmployeeModel;
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
    public void toInCheck(String id) {
        changeState(id, EmployeeState.IN_CHECK);
    }

    @Override
    public void toApprove(String id) {
        changeState(id, EmployeeState.APPROVED);
    }

    @Override
    public void toActivate(String id) {
        changeState(id, EmployeeState.ACTIVE);
    }

    @Override
    public Optional<EmployeeModel> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    private void changeState(String id, EmployeeState state) {
        EmployeeDto employeeDto = getEmployeeDto(id, state);
        kafkaTemplate.send(employeeTopic, employeeDto);
    }

    private EmployeeDto getEmployeeDto(String id, EmployeeState state) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(id);
        employeeDto.setEmployeeState(state);
        return employeeDto;
    }
}
