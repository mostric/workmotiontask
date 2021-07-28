package com.workmotion.core.entities;

import com.workmotion.core.enums.EmployeeState;
import lombok.Data;

@Data
public class EmployeeDto {

    private String id;

    private String name;

    private Integer age;

    private String skills;

    private String contractInformation;

    private EmployeeState employeeState;
}
