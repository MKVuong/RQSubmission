package com.reliaquest.api.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.reliaquest.api.model.ApiDataResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiClient apiClient;

    private final String baseUrl = "https://jank-api.com/employees";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        apiClient = new ApiClient(restTemplate);
        try { // Manually inject baseUrl since @Value won't populate in test
            Field field = apiClient.getClass().getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(apiClient, baseUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllEmployeesReturnsListOnSuccess() {
        List<Employee> mockEmployees = List.of(new Employee("1", "fred", 1000, null, null, null));
        ApiDataResponse<List<Employee>> responseBody = new ApiDataResponse<>();
        responseBody.setData(mockEmployees);

        ResponseEntity<ApiDataResponse<List<Employee>>> response = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                        eq(baseUrl),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<List<Employee>>>>any()))
                .thenReturn(response);

        List<Employee> result = apiClient.getAllEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("fred", result.get(0).getName());
    }

    @Test
    void getAllEmployeesReturnsNullOnException() {
        when(restTemplate.exchange(
                        eq(baseUrl),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<List<Employee>>>>any()))
                .thenThrow(new RuntimeException("Some failure"));

        List<Employee> result = apiClient.getAllEmployees();
        assertNull(result);
    }

    @Test
    void getEmployeeByIdReturnsEmployeeOnSuccess() {
        Employee mockEmployee = new Employee("2", "bob", 2000, null, null, null);
        ApiDataResponse<Employee> responseBody = new ApiDataResponse<>();
        responseBody.setData(mockEmployee);

        ResponseEntity<ApiDataResponse<Employee>> response = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                        eq(baseUrl + "/{id}"),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<Employee>>>any(),
                        eq("2")))
                .thenReturn(response);

        Employee result = apiClient.getEmployeeById("2");
        assertNotNull(result);
        assertEquals("bob", result.getName());
    }

    @Test
    void getEmployeeByIdReturnsNullOnNotFound() {
        when(restTemplate.exchange(
                        eq(baseUrl + "/{id}"),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<Employee>>>any(),
                        eq("2")))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Employee result = apiClient.getEmployeeById("2");
        assertNull(result);
    }

    @Test
    void addEmployeeSuccess() {
        EmployeeInput input = new EmployeeInput("nancy", 3000, 24, "student");
        Employee createdEmployee = new Employee("3", "nancy", 3000, 24, "student", null);
        ApiDataResponse<Employee> responseBody = new ApiDataResponse<>();
        responseBody.setData(createdEmployee);

        ResponseEntity<ApiDataResponse<Employee>> response = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                        eq(baseUrl),
                        eq(HttpMethod.POST),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<Employee>>>any()))
                .thenReturn(response);

        Employee result = apiClient.addEmployee(input);
        assertNotNull(result);
        assertEquals("nancy", result.getName());
    }

    @Test
    void addEmployeeReturnsNullOnFailure() {
        EmployeeInput input = new EmployeeInput("nancy", 3000, null, "medic");

        when(restTemplate.exchange(
                        eq(baseUrl),
                        eq(HttpMethod.POST),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<Employee>>>any()))
                .thenThrow(new RuntimeException("Some error"));

        Employee result = apiClient.addEmployee(input);
        assertNull(result);
    }

    @Test
    void deleteEmployeeByNameReturnsNameOnSuccess() {
        ApiDataResponse<Boolean> responseBody = new ApiDataResponse<>();
        responseBody.setData(true);

        ResponseEntity<ApiDataResponse<Boolean>> response = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                        eq(baseUrl),
                        eq(HttpMethod.DELETE),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiDataResponse<Boolean>>>any()))
                .thenReturn(response);

        String result = apiClient.deleteEmployeeByName("jeff");
        assertEquals("jeff", result);
    }
}
