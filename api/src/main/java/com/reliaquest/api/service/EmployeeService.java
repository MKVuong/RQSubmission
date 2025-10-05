package com.reliaquest.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.reliaquest.api.client.ApiClient;
import com.reliaquest.api.model.Employee;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    // use client to call the endpoint and 
    // get the mockEmployees from server api
    
    private final ApiClient apiClient;

    public List<Employee> getAll() {
        return apiClient.getAll();
    }

    public List<Employee> getTopTenHighestEarningEmployeeNames() {
        return null;
        // implement java stream to sort returned list of employees by highest salary, 
        // then returning the top 10

       // return apiClient.getAll().stream()
    }

}
