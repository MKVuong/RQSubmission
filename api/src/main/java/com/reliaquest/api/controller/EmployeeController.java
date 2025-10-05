package com.reliaquest.api.controller;

import com.reliaquest.api.model.EmployeeModel;
import com.reliaquest.api.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import com.reliaquest.api.model.EmployeeInputModel;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<EmployeeModel, EmployeeInputModel> {

    private final EmployeeService employeeService;
    
    @Override
    public ResponseEntity<List<EmployeeModel>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @Override
    public ResponseEntity<List<EmployeeModel>> getEmployeesByNameSearch(String name) {
        return null;
    }

    @Override
    public ResponseEntity<EmployeeModel> getEmployeeById(String id) {
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
    public ResponseEntity<EmployeeModel> createEmployee(EmployeeInputModel input) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
    
}
