package com.workmotion.controllers;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
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
        return ResponseEntity.ok().body(kafkaEmployeeService.createEmployee(employeeDto));
    }

    @PostMapping("/{id}/toInCheck")
    public ResponseEntity<EmployeeDto> toInCheck(@PathVariable String id) throws EmployeeNotFoundException {
        kafkaEmployeeService.toInCheck(id);
        return ResponseEntity.ok().body(kafkaEmployeeService.toInCheck(id));
    }

    @PostMapping("/{id}/toApprove")
    public ResponseEntity<EmployeeDto> toApprove(@PathVariable String id) throws EmployeeNotFoundException {
        return ResponseEntity.ok().body(kafkaEmployeeService.toApprove(id));
    }

    @PostMapping("/{id}/toActivate")
    public ResponseEntity<EmployeeDto> toActivate(@PathVariable String id) throws EmployeeNotFoundException {
        return ResponseEntity.ok().body(kafkaEmployeeService.toActivate(id));
    }

    @GetMapping("/{id}/state")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<EmployeeDto> state(@PathVariable String id) throws EmployeeNotFoundException {
        return ResponseEntity.ok(kafkaEmployeeService.getStateForEmployeeId(id));
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
