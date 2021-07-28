package com.workmotion.repositories;

import com.workmotion.entities.EmployeeModel;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<EmployeeModel, String> {
}
