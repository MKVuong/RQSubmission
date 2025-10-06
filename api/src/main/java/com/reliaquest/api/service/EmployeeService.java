package com.reliaquest.api.service;

import com.reliaquest.api.client.ApiClient;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    // use client to call Server API to interact with its mock employees

    private final ApiClient apiClient;

    public List<Employee> getAll() {
        return apiClient.getAllEmployees();
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        return getAll().stream()
                .filter(e -> e.getName().toLowerCase().contains(searchString.toLowerCase()))
                .toList();
    }

    public Employee getEmployeeById(String id) {
        return apiClient.getEmployeeById(id);
    }

    public Integer getHighestSalaryOfEmployees() {
        Optional<Employee> employeeWithHighestSalary =
                getAll().stream().max(Comparator.comparingInt(Employee::getSalary));
        return employeeWithHighestSalary.isPresent()
                ? employeeWithHighestSalary.get().getSalary()
                : null;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        return getAll().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .toList(); // Replace this line with below comment for manual validation

        // .map(e -> e.getName() + " - $" + e.getSalary()).toList();
    }

    public Employee createEmployee(EmployeeInput input) {
        return apiClient.addEmployee(input);
    }

    public String deleteEmployeeById(String id) {
        return Optional.ofNullable(getEmployeeById(id))
                .map(employee -> apiClient.deleteEmployeeByName(employee.getName()))
                .orElse(null);
    }
}
