package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;

public class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployeesReturnOkWhenEmployeesExist() {
        
        List<Employee> mockEmployees = List.of(new Employee("1", "chad", 50, 74, "Beans Engineer", "chad@hotmail.com"));
        when(employeeService.getAll()).thenReturn(mockEmployees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllEmployeesShouldReturnNoContentWhenEmployeeListIsEmpty() {
        
        when(employeeService.getAll()).thenReturn(List.of());

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getAllEmployeesShouldReturnInternalServerErrorWhenServiceReturnsNull() {

        when(employeeService.getAll()).thenReturn(null);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
