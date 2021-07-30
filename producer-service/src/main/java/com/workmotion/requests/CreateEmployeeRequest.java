package com.workmotion.requests;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private String name;
    private Integer age;
    private String skills;
    private String contractInformation;
}
