package com.workmotion.listener;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.services.EmployeeStateMachineProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;


@Slf4j
@AllArgsConstructor
public class EmployeeEventListener {

    private final EmployeeStateMachineProcessor employeeStateMachineProcessor;

    @KafkaListener(topics = "employee-topic")
    public void consumeEmployeeEvent(EmployeeDto employee) {
        log.info("Starting employee process with id {} ...", employee.getId());
        employeeStateMachineProcessor.process(employee);
    }
}
