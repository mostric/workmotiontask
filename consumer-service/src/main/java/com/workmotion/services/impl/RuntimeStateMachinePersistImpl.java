package com.workmotion.services.impl;

import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeStateMachinePersistImpl implements StateMachinePersist<EmployeeState, EmployeeStateEvent, String> {

    private final Map<String, StateMachineContext<EmployeeState, EmployeeStateEvent>> map = new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<EmployeeState, EmployeeStateEvent> context, String contextObj) {
        map.put(contextObj, context);
    }

    @Override
    public StateMachineContext<EmployeeState, EmployeeStateEvent> read(String contextObj) {
        return map.get(contextObj);
    }
}
