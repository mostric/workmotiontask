package com.workmotion.controllers;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.entities.EmployeeModel;
import com.workmotion.requests.CreateEmployeeRequest;
import com.workmotion.services.KafkaEmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final KafkaEmployeeService kafkaEmployeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody CreateEmployeeRequest request) {
        EmployeeDto employeeDto = populateEmployeeDto(request);
        return ResponseEntity
                .ok()
                .body(kafkaEmployeeService.createEmployee(employeeDto));
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<Void> toInCheck(@PathVariable String id) throws EmployeeNotFoundException {
        kafkaEmployeeService.toInCheck(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> toApprove(@PathVariable String id) throws EmployeeNotFoundException {
        kafkaEmployeeService.toApprove(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> toActivate(@PathVariable String id) throws EmployeeNotFoundException {
        kafkaEmployeeService.toActivate(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/state")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<EmployeeState> state(@PathVariable String id) throws EmployeeNotFoundException {
        EmployeeModel employeeModel = kafkaEmployeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeModel.getEmployeeState());
    }

    private EmployeeDto populateEmployeeDto(CreateEmployeeRequest request) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(request.getName());
        employeeDto.setAge(request.getAge());
        employeeDto.setSkills(request.getSkills());
        return employeeDto;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> onEmployeeNotFoundException(EmployeeNotFoundException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onGeneralException(Exception ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
