package com.reliaquest.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiClient {

    /*- TODO:
    Core implementations for happy paths,
    exception handling and specifically error 429 with some Retry mechanism,
    logging,
    testing
    */

    private final RestTemplate restTemplate;

    HttpEntity<String> httpEntity;

    String baseUrl = "http://localhost:8112/api/v1/employee";

    public List<Employee> getAllEmployees() {
        log.info("Entered ApiClient's getAllEmployees method...");
        try {
            ResponseEntity<ApiDataResponse<List<Employee>>> responseEntity = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiDataResponse<List<Employee>>>() {});

            if (responseEntity != null && responseEntity.hasBody()) {
                log.info("Printing responseEntity.getBody(): \n{}", responseEntity.getBody());
                return Optional.ofNullable(responseEntity.getBody())
                        .map(ApiDataResponse::getData)
                        .orElse(null);
            }
        } catch (Exception e) {
            log.warn("Exception occurred during api client's getAllEmployees method with error: {}", e);
        }
        return null;
    }

    public Employee getEmployeeById(String id) {
        log.info("Entered ApiClient's getEmployeeById method with id: {}.", id);
        try {
            ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiDataResponse<Employee>>() {},
                    id);
            if (response.getStatusCode().is2xxSuccessful()) {
                Employee employee = response.getBody().getData();
                log.info("Data of Response Body: {}", employee);
                log.info("HTTP Status Code: {}", response.getStatusCode());
                return employee;
            } else {
                log.warn("Unsuccessful: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException hcee) {
            if (hcee.getStatusCode().value() == 429) {
                log.error(
                        "Too Many Requests: 429 error occurred during api client's getEmployeeById method with error: {}",
                        hcee.getMessage(),
                        hcee);
                // retry mechanism
            } else {
                log.error(
                        "HttpClientErrorException occurred during api client's getEmployeeById with error: {} and Status Code: {}",
                        hcee.getMessage(),
                        hcee.getStatusCode(),
                        hcee);
            }
        } catch (Exception e) {
            log.error("Exception occurred during api client's getEmployeeById with error: {}", e.getMessage(), e);
        }
        return null;
    }

    public Employee addEmployee(EmployeeInput employeeInput) {
        log.info("Entered ApiClient's addEmployee method with employeeInput: {}.", employeeInput);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeInput> entity = new HttpEntity<>(employeeInput, headers);

        ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(
                baseUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<ApiDataResponse<Employee>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info(
                    "Employee creation successful with statusCode: {} and body: {}.",
                    response.getStatusCode(),
                    response.getBody().getData());
            return response.getBody().getData();
        } else {
            log.warn("Unsuccessful: " + response.getStatusCode());
        }
        return null;
    }

    public String deleteEmployeeByName(String nameInput) {
        log.info("Entered ApiClient's deleteEmployeeByName method with name: {}.", nameInput);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<String> entity =
                    new HttpEntity<>(new ObjectMapper().writeValueAsString(Map.of("name", nameInput)), headers);

            ResponseEntity<ApiDataResponse<Boolean>> response = restTemplate.exchange(
                    baseUrl, HttpMethod.DELETE, entity, new ParameterizedTypeReference<ApiDataResponse<Boolean>>() {});
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info(
                        "Employee deletion successful with statusCode: {} and body: {}.",
                        response.getStatusCode(),
                        response.getBody().getData());
                return nameInput;
            } else {
                log.warn("Unsuccessful: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException hcee) {
            if (hcee.getStatusCode().value() == 429) {
                log.error(
                        "Too Many Requests: 429 error occurred during api client's deleteEmployeeByName method with error: {}",
                        hcee.getMessage(),
                        hcee);
                // retry mechanism
            } else {
                log.error(
                        "HttpClientErrorException occurred during api client's deleteEmployeeByName with error: {} and Status Code: {}",
                        hcee.getMessage(),
                        hcee.getStatusCode(),
                        hcee);
            }
        } catch (Exception e) {
            log.error("Exception occurred during api client's deleteEmployeeByName with error: {}", e.getMessage(), e);
        }

        return null;
    }
}
