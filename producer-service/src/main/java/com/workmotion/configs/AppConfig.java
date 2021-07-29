package com.workmotion.configs;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.repositories.EmployeeRepository;
import com.workmotion.services.KafkaEmployeeService;
import com.workmotion.services.impl.KafkaEmployeeServiceImpl;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class AppConfig {

    @Value(value = "employee-topic")
    private String employeeTopic;

    @Bean
    public NewTopic employeeTopic() {
        int numPartitions = 1;
        short replicationFactor = 1;
        return new NewTopic(employeeTopic, numPartitions, replicationFactor);
    }

    @Bean
    public KafkaEmployeeService employeeService(EmployeeRepository employeeRepository,
                                                KafkaTemplate<String, EmployeeDto> kafkaTemplate) {
        return new KafkaEmployeeServiceImpl(employeeRepository, kafkaTemplate, employeeTopic);
    }

    @Bean
    @Profile("!deploy")
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @Profile("deploy")
    public Docket apiDeploy() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
