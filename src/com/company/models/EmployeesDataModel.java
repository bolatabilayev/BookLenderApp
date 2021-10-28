package com.company.models;

import com.company.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeesDataModel {
    private Employee employee;
    private List<Employee> employees = new ArrayList<>();
    private BookDataModel bookDataModel = new BookDataModel();

    public EmployeesDataModel(){
        Employee marilyn = new Employee("Marilyn", "Monroe@mail.ru");
        Employee john = new Employee("John", "Doe@mail.com");
        Employee michael = new Employee("Michael", "Jordan@email.com");
        marilyn.setImgPath("photo1.jpg");
        john.setImgPath("photo2.jpg");
        michael.setImgPath("photo3.jpg");

        employees.add(marilyn);
        employees.add(john);
        employees.add(michael);
        employees.get(0).takeABook(bookDataModel.getBooks().get(0));
        employees.get(1).takeABook(bookDataModel.getBooks().get(1));
        employees.get(2).takeABook(bookDataModel.getBooks().get(2));
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


}
