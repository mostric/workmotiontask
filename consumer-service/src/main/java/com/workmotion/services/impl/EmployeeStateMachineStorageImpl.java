package com.workmotion.services.impl;

import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import com.workmotion.services.EmployeeStateMachineStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

@AllArgsConstructor
@Slf4j
public class EmployeeStateMachineStorageImpl implements EmployeeStateMachineStorage {

    private final StateMachinePersister<EmployeeState, EmployeeStateEvent, String> stateMachinePersister;
    private final StateMachineFactory<EmployeeState, EmployeeStateEvent> stateMachineFactory;

    @Override
    public StateMachine<EmployeeState, EmployeeStateEvent> getStateMachine(String employeeId) {
        StateMachine<EmployeeState, EmployeeStateEvent> stateMachine = stateMachineFactory.getStateMachine();
        try {
            return stateMachinePersister.restore(stateMachine, employeeId);
        } catch (Exception ex) {
            log.error("Something went wrong while restoring state machine '{}' for employee '{}'."
                    , stateMachine.getId(), employeeId);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveStateMachine(String employeeId, StateMachine<EmployeeState, EmployeeStateEvent> stateMachine) {
        try {
            stateMachinePersister.persist(stateMachine, employeeId);
        } catch (Exception ex) {
            log.error("Something went wrong while saving state machine '{}' for employee '{}'."
                    , stateMachine.getId(), employeeId);
            throw new RuntimeException(ex);
        }
    }

}
