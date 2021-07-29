package com.workmotion.actions;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import com.workmotion.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static com.workmotion.consts.EmployeeConsts.EMPLOYEE_PARAM;

@AllArgsConstructor
@Slf4j
public class ChangeEmployeeStateAction implements Action<EmployeeState, EmployeeStateEvent> {

    private final EmployeeService employeeService;

    @SneakyThrows
    @Override
    public void execute(StateContext<EmployeeState, EmployeeStateEvent> stateContext) {
        EmployeeDto employeeDto = (EmployeeDto) stateContext
                .getStateMachine()
                .getExtendedState()
                .getVariables()
                .get(EMPLOYEE_PARAM);
        EmployeeState nextState = stateContext.getTarget().getId();
        log.info("Changing employee state from '{}' to '{}'...", employeeDto.getEmployeeState(), nextState);
        employeeDto.setEmployeeState(nextState);
        employeeService.changeEmployeeState(employeeDto);
    }
}
