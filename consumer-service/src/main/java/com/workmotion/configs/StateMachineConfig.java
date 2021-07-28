package com.workmotion.configs;

import com.workmotion.actions.ChangeEmployeeStateAction;
import com.workmotion.actions.CreateEmployeeAction;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import com.workmotion.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@EnableStateMachineFactory
@Configuration
@AllArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeStateEvent> {

    private final EmployeeService employeeService;

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeStateEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeStateEvent> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeState.ADDED)
                .end(EmployeeState.ACTIVE)
                .states(EnumSet.allOf(EmployeeState.class));

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeStateEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .target(EmployeeState.ADDED)
                .event(EmployeeStateEvent.CREATE)
                .action(addAction())

                .and()
                .withExternal()
                .source(EmployeeState.ADDED)
                .target(EmployeeState.IN_CHECK)
                .event(EmployeeStateEvent.TO_IN_CHECK)
                .action(changeEmployeeStateAction())

                .and()
                .withExternal()
                .source(EmployeeState.IN_CHECK)
                .target(EmployeeState.APPROVED)
                .event(EmployeeStateEvent.TO_APPROVE)
                .action(changeEmployeeStateAction())

                .and()
                .withExternal()
                .source(EmployeeState.APPROVED)
                .target(EmployeeState.ACTIVE)
                .event(EmployeeStateEvent.TO_ACTIVATE)
                .action(changeEmployeeStateAction());
    }

    @Bean
    public Action<EmployeeState, EmployeeStateEvent> addAction() {
        return new CreateEmployeeAction(employeeService);
    }

    @Bean
    public Action<EmployeeState, EmployeeStateEvent> changeEmployeeStateAction() {
        return new ChangeEmployeeStateAction(employeeService);
    }
}
