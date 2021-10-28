package com.company.context;

import com.company.Employee;

import java.util.Optional;

public interface DataContext {
    Optional<Employee> getEmployee(String email);
    boolean addUser(Employee employee);
}
