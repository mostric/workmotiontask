package com.workmotion.entities;

import com.workmotion.core.enums.EmployeeState;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "employee")
@Entity
@Data
public class EmployeeModel {

    @Id
    String id;

    @Column
    String name;

    @Column
    Integer age;

    @Column
    String skills;

    @Column
    EmployeeState employeeState;
}
