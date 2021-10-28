package com.company.context;

import com.company.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JsonContext implements DataContext{
    Gson gson;

    public JsonContext() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public Optional<Employee> getEmployee(String email) {
        try(FileReader fileReader = new FileReader("data/json/employees.json")) {
            List<Employee> employees = gson.fromJson(fileReader, new TypeToken<List<Employee>>(){}.getType());
            return employees.stream().filter(e -> e.getEmail().equals(email)).findFirst();
        } catch (IOException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean addUser(Employee employee) {
        return false;
    }
}
