package com.company.models;

import com.company.Employee;

public class EmployeeModel {
    private Employee employee;

    public EmployeeModel(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
