package com.workmotion.services;

import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import org.springframework.statemachine.StateMachine;

public interface EmployeeStateMachineStorage {

    StateMachine<EmployeeState, EmployeeStateEvent> getStateMachine(String employeeId);

    void saveStateMachine(String employeeId, StateMachine<EmployeeState, EmployeeStateEvent> stateMachine);

}
