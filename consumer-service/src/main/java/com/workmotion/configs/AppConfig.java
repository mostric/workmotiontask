package com.workmotion.configs;

import com.workmotion.core.enums.EmployeeState;
import com.workmotion.enums.EmployeeStateEvent;
import com.workmotion.listener.EmployeeEventListener;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.services.EmployeeService;
import com.workmotion.services.EmployeeStateMachineProcessor;
import com.workmotion.services.EmployeeStateMachineStorage;
import com.workmotion.services.impl.EmployeeServiceImpl;
import com.workmotion.services.impl.EmployeeStateMachineProcessorImpl;
import com.workmotion.services.impl.EmployeeStateMachineStorageImpl;
import com.workmotion.services.impl.RuntimeStateMachinePersistImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeStateMachineStorage employeeStateMachineStorage(
            StateMachinePersister<EmployeeState, EmployeeStateEvent, String> stateMachinePersister,
            StateMachineFactory<EmployeeState, EmployeeStateEvent> stateMachineFactory) {
        return new EmployeeStateMachineStorageImpl(stateMachinePersister, stateMachineFactory);
    }

    @Bean
    public EmployeeStateMachineProcessor stateProcessor(EmployeeStateMachineStorage employeeStateMachineStorage) {
        return new EmployeeStateMachineProcessorImpl(employeeStateMachineStorage);
    }

    @Bean
    public StateMachinePersister<EmployeeState, EmployeeStateEvent, String> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new RuntimeStateMachinePersistImpl());
    }

    @Bean
    public EmployeeService employeeService(EmployeeRepository employeeRepository) {
        return new EmployeeServiceImpl(employeeRepository);
    }

    @Bean
    public EmployeeEventListener employeeEventListener(EmployeeStateMachineProcessor stateProcessor) {
        return new EmployeeEventListener(stateProcessor);
    }

    @Bean
    public RecordMessageConverter messageConverter() {
        return new StringJsonMessageConverter();
    }
}
