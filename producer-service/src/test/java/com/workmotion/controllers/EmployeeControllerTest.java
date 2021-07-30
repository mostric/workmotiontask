package com.workmotion.controllers;

import com.workmotion.core.entities.EmployeeDto;
import com.workmotion.core.enums.EmployeeState;
import com.workmotion.core.exceptions.EmployeeNotFoundException;
import com.workmotion.services.KafkaEmployeeService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    public static final String EMPLOYEE_ID = "EMPLOYEE_ID_123";
    public static final String NOT_EXISTING_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String ERROR_MESSAGE = "Error message";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private KafkaEmployeeService kafkaEmployeeService;

    // ==================================SUCCESS_MESSAGES==============================
    @Test
    void createEmployeeReturns200OKWithResponse() throws Exception {
        EmployeeDto createdEmployee = new EmployeeDto();
        createdEmployee.setId(EMPLOYEE_ID);
        when(kafkaEmployeeService.createEmployee(any(EmployeeDto.class))).thenReturn(createdEmployee);

        mvc.perform(post("/employee").content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(EMPLOYEE_ID)));

        verify(kafkaEmployeeService).createEmployee(any(EmployeeDto.class));
    }

    @Test
    public void movingToInCheckReturns200OKWithResponse() throws Exception {
        EmployeeDto createdEmployee = new EmployeeDto();
        createdEmployee.setId(EMPLOYEE_ID);
        createdEmployee.setEmployeeState(EmployeeState.IN_CHECK);

        when(kafkaEmployeeService.toInCheck(eq(EMPLOYEE_ID))).thenReturn(createdEmployee);

        mvc.perform(post("/employee/{id}/toInCheck", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(EMPLOYEE_ID)))
                .andExpect(jsonPath("$.employeeState", Is.is(EmployeeState.IN_CHECK.toString())));
    }

    @Test
    public void movingToApproveReturns200OKWithResponse() throws Exception {
        EmployeeDto createdEmployee = new EmployeeDto();
        createdEmployee.setId(EMPLOYEE_ID);
        createdEmployee.setEmployeeState(EmployeeState.APPROVED);

        when(kafkaEmployeeService.toApprove(eq(EMPLOYEE_ID)))
                .thenReturn(createdEmployee);

        mvc.perform(post("/employee/{id}/toApprove", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(EMPLOYEE_ID)))
                .andExpect(jsonPath("$.employeeState", Is.is(EmployeeState.APPROVED.toString())));
    }

    @Test
    public void movingToActivateReturns200OKWithResponse() throws Exception {
        EmployeeDto createdEmployee = new EmployeeDto();
        createdEmployee.setId(EMPLOYEE_ID);
        createdEmployee.setEmployeeState(EmployeeState.ACTIVE);

        when(kafkaEmployeeService.toActivate(eq(EMPLOYEE_ID))).thenReturn(createdEmployee);

        mvc.perform(post("/employee/{id}/toActivate", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(EMPLOYEE_ID)))
                .andExpect(jsonPath("$.employeeState", Is.is(EmployeeState.ACTIVE.toString())));
    }

    @Test
    public void gettingStateReturns200OKWithResponse() throws Exception {
        EmployeeDto createdEmployee = new EmployeeDto();
        createdEmployee.setId(EMPLOYEE_ID);
        createdEmployee.setEmployeeState(EmployeeState.ACTIVE);

        when(kafkaEmployeeService.toActivate(eq(EMPLOYEE_ID))).thenReturn(createdEmployee);

        mvc.perform(post("/employee/{id}/toActivate", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(EMPLOYEE_ID)))
                .andExpect(jsonPath("$.employeeState", Is.is(EmployeeState.ACTIVE.toString())));
    }

    // ==================================ERROR_MESSAGES==============================
    @Test
    public void creatingEmployeeReturns500WithMessageWhenGenericError() throws Exception {
        when(kafkaEmployeeService.createEmployee(any(EmployeeDto.class))).thenThrow(new IllegalStateException(ERROR_MESSAGE));

        mvc.perform(post("/employee").content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(ERROR_MESSAGE));
    }

    @Test
    public void movingToInCheckEmployeeStateReturns404NotFoundWhenEmployeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(NOT_EXISTING_EMPLOYEE_ID))
                .when(kafkaEmployeeService).toInCheck(NOT_EXISTING_EMPLOYEE_ID);

        mvc.perform(post("/employee/{id}/toInCheck", NOT_EXISTING_EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Employee with id %s not found.", NOT_EXISTING_EMPLOYEE_ID)));
    }

    @Test
    public void movingToApproveEmployeeStateReturns404NotFoundWhenEmployeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(NOT_EXISTING_EMPLOYEE_ID))
                .when(kafkaEmployeeService).toApprove(NOT_EXISTING_EMPLOYEE_ID);

        mvc.perform(post("/employee/{id}/toApprove", NOT_EXISTING_EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Employee with id %s not found.", NOT_EXISTING_EMPLOYEE_ID)));
    }

    @Test
    public void movingToActivateEmployeeStateReturns404NotFoundWhenEmployeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(NOT_EXISTING_EMPLOYEE_ID))
                .when(kafkaEmployeeService).toActivate(NOT_EXISTING_EMPLOYEE_ID);

        mvc.perform(post("/employee/{id}/toActivate", NOT_EXISTING_EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Employee with id %s not found.", NOT_EXISTING_EMPLOYEE_ID)));
    }

    @Test
    public void gettingEmployeeStateReturns404NotFoundWhenEmployeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(NOT_EXISTING_EMPLOYEE_ID))
                .when(kafkaEmployeeService).getStateForEmployeeId(NOT_EXISTING_EMPLOYEE_ID);

        mvc.perform(get("/employee/{id}/state", NOT_EXISTING_EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Employee with id %s not found.", NOT_EXISTING_EMPLOYEE_ID)));
    }

}
