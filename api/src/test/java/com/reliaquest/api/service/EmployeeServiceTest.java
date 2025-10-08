package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.ApiClient;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTest {

    private ApiClient apiClient;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        apiClient = mock(ApiClient.class);
        employeeService = new EmployeeService(apiClient);
    }

    @Test
    void testGetAll() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Alice", 1000, 18, "Software Engineer", "alice@gmail.com"),
                new Employee("2", "Bob", 2000, 19, "Mechanic", "bob@hotmail.com"));
        when(apiClient.getAllEmployees()).thenReturn(employees);

        List<Employee> result = employeeService.getAll();
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void testGetEmployeesByNameSearch() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Alice", 1000, null, null, null),
                new Employee("2", "Bob", 2000, null, null, null),
                new Employee("3", "Alicia", 1500, null, null, null));
        when(apiClient.getAllEmployees()).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesByNameSearch("ali");
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alicia")));
    }

    @Test
    void testGetEmployeesByNameSearch_NullList() {
        when(apiClient.getAllEmployees()).thenReturn(null);
        assertNull(employeeService.getEmployeesByNameSearch("test"));
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee("1", "Alice", 1000, null, null, null);
        when(apiClient.getEmployeeById("1")).thenReturn(employee);

        Employee result = employeeService.getEmployeeById("1");
        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Alice", 1000, 0, null, null),
                new Employee("2", "Bob", 3000, 0, null, null),
                new Employee("3", "Charlie", 2000, 0, null, null));
        when(apiClient.getAllEmployees()).thenReturn(employees);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(3000, highestSalary);
    }

    @Test
    void testGetHighestSalaryOfEmployees_EmptyList() {
        when(apiClient.getAllEmployees()).thenReturn(Collections.emptyList());
        assertNull(employeeService.getHighestSalaryOfEmployees());
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Alice", 1000, null, null, null),
                new Employee("2", "Bob", 3000, null, null, null),
                new Employee("3", "Charlie", 2000, null, null, null),
                new Employee("4", "David", 4000, null, null, null),
                new Employee("5", "Eve", 500, null, null, null),
                new Employee("6", "Frank", 3500, null, null, null),
                new Employee("7", "Grace", 2500, null, null, null),
                new Employee("8", "Heidi", 17, null, null, null),
                new Employee("9", "Ivan", 800, null, null, null),
                new Employee("10", "Judy", 900, null, null, null),
                new Employee("11", "Mallory", 600, null, null, null));
        when(apiClient.getAllEmployees()).thenReturn(employees);

        List<String> topTen = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(10, topTen.size());
        assertFalse(topTen.contains("Heidi"));
    }

    @Test
    void testCreateEmployee() {
        EmployeeInput input = new EmployeeInput("Sam", 1500, 23, "Babysitter");
        Employee employee = new Employee("12", "Sam", 1500, 23, "Babysitter", "sam@boa.com");
        when(apiClient.addEmployee(input)).thenReturn(employee);

        Employee result = employeeService.createEmployee(input);
        assertNotNull(result);
        assertEquals("Sam", result.getName());
    }

    @Test
    void testDeleteEmployeeByIdSuccess() {
        Employee employee = new Employee("1", "Alice", 1000, null, null, null);
        when(apiClient.getEmployeeById("1")).thenReturn(employee);
        when(apiClient.deleteEmployeeByName("Alice")).thenReturn("Alice");

        String result = employeeService.deleteEmployeeById("1");
        assertEquals("Alice", result);
    }

    @Test
    void testDeleteEmployeeByIdNotFound() {
        when(apiClient.getEmployeeById("99")).thenReturn(null);

        String result = employeeService.deleteEmployeeById("99");
        assertNull(result);
    }
}
