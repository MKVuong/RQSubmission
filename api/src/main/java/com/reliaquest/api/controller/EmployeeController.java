package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import com.reliaquest.api.model.EmployeeInput;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput> {

    private final EmployeeService employeeService;
    
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String name) {
        return null;
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeInput input) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
    
}
