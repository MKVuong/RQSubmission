package com.reliaquest.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.config.ApiRetryable;
import com.reliaquest.api.model.ApiDataResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiClient {

    private final RestTemplate restTemplate;

    private final String baseUrl;

    HttpEntity<String> httpEntity;

    @ApiRetryable
    public List<Employee> getAllEmployees() {
        log.info("Entered ApiClient.getAllEmployees method.");
        try {
            ResponseEntity<ApiDataResponse<List<Employee>>> responseEntity = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiDataResponse<List<Employee>>>() {});

            if (responseEntity != null && responseEntity.hasBody()) {
                log.info("Printing responseEntity's body: {}", responseEntity.getBody());
                return Optional.ofNullable(responseEntity.getBody())
                        .map(ApiDataResponse::getData)
                        .orElse(null);
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                log.warn("Too Many Requests with error code 429.");
                throw ex;
            }
        } catch (Exception e) {
            log.error("Exception occurred during api client's getAllEmployees method with error: {}", e.getMessage());
        }
        return null;
    }

    @ApiRetryable
    public Employee getEmployeeById(String id) {
        log.info("Entered ApiClient.getEmployeeById method with id: {}.", id);
        try {
            ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiDataResponse<Employee>>() {},
                    id);
            if (response != null
                    && response.hasBody()
                    && response.getStatusCode().is2xxSuccessful()) {
                Employee employee = response.getBody().getData();
                log.info("Employee found with id: {} and details: {}.", id, employee);
                return employee;
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                log.warn("Too Many Requests with error code 429.");
                throw ex;
            } else if (ex.getStatusCode().value() == 404) {
                log.warn("Employee with id: {} not found.", id);
            }
        } catch (Exception e) {
            log.error("Exception occurred during ApiClient.getEmployeeById method with error: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    @ApiRetryable
    public Employee addEmployee(EmployeeInput employeeInput) {
        log.info("Entered ApiClient.addEmployee method with employeeInput: {}.", employeeInput);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeInput> entity = new HttpEntity<>(employeeInput, headers);

        try {
            ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(
                    baseUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<ApiDataResponse<Employee>>() {});
            if (response != null
                    && response.hasBody()
                    && response.getStatusCode().is2xxSuccessful()) {
                log.info(
                        "Employee creation successful with statusCode: {} and data: {}.",
                        response.getStatusCode(),
                        response.getBody().getData());
                return response.getBody().getData();
            } else {
                log.warn("Employee creation unsuccessful: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                log.warn("Too Many Requests with error code 429.");
                throw ex;
            }
        } catch (Exception e) {
            log.error("Exception occurred during ApiClient.addEmployee method with error: {}", e.getMessage());
        }
        return null;
    }

    @ApiRetryable
    public String deleteEmployeeByName(String nameInput) {
        log.info("Entered ApiClient.deleteEmployeeByName method with name: {}.", nameInput);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<String> entity =
                    new HttpEntity<>(new ObjectMapper().writeValueAsString(Map.of("name", nameInput)), headers);

            ResponseEntity<ApiDataResponse<Boolean>> response = restTemplate.exchange(
                    baseUrl, HttpMethod.DELETE, entity, new ParameterizedTypeReference<ApiDataResponse<Boolean>>() {});
            if (response != null
                    && response.hasBody()
                    && response.getStatusCode().is2xxSuccessful()) {
                log.info(
                        "Employee deletion successful with statusCode: {} and data: {}.",
                        response.getStatusCode(),
                        response.getBody().getData());
                return nameInput;
            } else {
                log.warn("Employee deletion unsuccessful: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                log.warn("Too Many Requests with error code 429.");
                throw ex;
            }
        } catch (JsonProcessingException e) {
            log.error(
                    "JsonProcessingException occurred during ApiClient.deleteEmployeeByName method with error: {}",
                    e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Exception occurred during ApiClient.deleteEmployeeByName method with error: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    @Recover
    public String retriesExhausted(HttpClientErrorException.TooManyRequests ex) {
        log.warn("Max retry attempts reached. Try again later...", ex.getMessage());
        return null;
    }
}
