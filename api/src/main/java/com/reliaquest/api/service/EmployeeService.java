package com.reliaquest.api.service;

import com.reliaquest.api.client.ApiClient;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;

import java.util.List;
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

    public Employee getEmployeeById(String id) {
        return apiClient.getEmployeeById(id);
    }

    public Employee createEmployee(EmployeeInput input) {
        return apiClient.addEmployee(input);
    }

    public List<Employee> getTopTenHighestEarningEmployeeNames() {
        return null;
        // implement java stream to sort returned list of employees by highest salary,
        // then returning the top 10

        // return apiClient.getAll().stream()
    }
}
