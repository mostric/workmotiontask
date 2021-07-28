package com.workmotion.services.impl;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import com.workmotion.services.EmployeeStateMachineProcessor;
import com.workmotion.services.EmployeeStateMachineStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.workmotion.Consts.EmployeeConsts.EMPLOYEE_PARAM;

@Slf4j
public class EmployeeStateMachineProcessorImpl implements EmployeeStateMachineProcessor {

    private static final Map<EmployeeState, EmployeeStateEvent> STATE2EVENT_MAP = new HashMap<>();

    static {
        STATE2EVENT_MAP.put(EmployeeState.IN_CHECK, EmployeeStateEvent.TO_IN_CHECK);
        STATE2EVENT_MAP.put(EmployeeState.APPROVED, EmployeeStateEvent.TO_APPROVE);
        STATE2EVENT_MAP.put(EmployeeState.ACTIVE, EmployeeStateEvent.TO_ACTIVATE);
    }

    private final EmployeeStateMachineStorage employeeStateMachineStorage;

    public EmployeeStateMachineProcessorImpl(EmployeeStateMachineStorage employeeStateMachineStorage) {
        this.employeeStateMachineStorage = employeeStateMachineStorage;
    }

    @Override
    public void process(EmployeeDto employeeDto) {
        String employeeId = employeeDto.getId();
        log.info("Starting to process employee '{}'...", employeeId);
        StateMachine<EmployeeState, EmployeeStateEvent> stateMachine =
                employeeStateMachineStorage.getStateMachine(employeeId);
        stateMachine.getExtendedState().getVariables().put(EMPLOYEE_PARAM, employeeDto);

        EmployeeStateEvent event = STATE2EVENT_MAP.get(employeeDto.getEmployeeState());
        Mono<Message<EmployeeStateEvent>> monoMessageEvent = Mono.just(MessageBuilder.withPayload(event).build());
        stateMachine.sendEvent(monoMessageEvent)
                .doOnComplete(() -> log.info("Event '{}' was sent.", event))
                .subscribe();
        employeeStateMachineStorage.saveStateMachine(employeeId, stateMachine);
    }
}
