package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput> {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAll();
        if (employees == null) {
            log.info("Error occurred while fetching employees.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else if (employees.isEmpty()) {
            log.info("No employees found.");
            return ResponseEntity.noContent().build();
        } else {
            log.info("Employees found: {}", employees.size());
            return ResponseEntity.ok(employees);
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
        if (employees == null) {
            log.info("Error occurred while searching employees with search string: {}", searchString);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else if (employees.isEmpty()) {
            log.info("No employees found with search string: {}", searchString);
            return ResponseEntity.noContent().build();
        } else {
            log.info("Employees found with search string '{}': {}", searchString, employees.size());
            return ResponseEntity.ok(employees);
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            if (employee == null) {
                log.info("No employee found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                log.info("Employee found with ID {}: {}", id, employee);
                return ResponseEntity.ok(employee);
            }
        } catch (Exception e) {
            log.error("Exception occurred during employee retrieval by id with error.", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
            if (highestSalary == null) {
                log.info("No employees found to determine the highest salary.");
                return ResponseEntity.noContent().build();
            } else {
                log.info("Highest salary among employees is: {}", highestSalary);
                return ResponseEntity.ok(highestSalary);
            }
        } catch (Exception e) {
            log.error("Exception occurred during highest salary retrieval with error.", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> topEarners = employeeService.getTopTenHighestEarningEmployeeNames();
            if (topEarners.isEmpty()) {
                log.info("No employees found to determine the top ten highest earners.");
                return ResponseEntity.noContent().build();
            } else {
                log.info("Top ten highest earning employees retrieved: {}", topEarners);
                return ResponseEntity.ok(topEarners);
            }
        } catch (Exception e) {
            log.error(
                    "Exception occurred during top ten highest earning employees retrieval with error.",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeInput input) {
        try {
            Employee createdEmployee = employeeService.createEmployee(input);
            if (createdEmployee == null) {
                log.info("Employee creation failed for input: {}", input);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                log.info("Employee created successfully: {}", createdEmployee);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
            }
        } catch (Exception e) {
            log.error("Exception occurred during employee creation with error.", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            String deletedEmployeeName = employeeService.deleteEmployeeById(id);
            if (deletedEmployeeName == null) {
                log.info("No employee found with ID: {} to delete.", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                log.info("Employee with ID: {} deleted successfully. Name: {}", id, deletedEmployeeName);
                return ResponseEntity.ok(deletedEmployeeName);
            }
        } catch (Exception e) {
            log.error("Exception occurred during employee deletion by id with error.", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
