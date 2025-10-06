package com.reliaquest.api.client;

import com.reliaquest.api.model.ApiDataResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;

import java.util.HashMap;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
        // HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.setContentType(MediaType.APPLICATION_JSON);
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
        //HttpHeaders headers = new HttpHeaders();
        //headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        //HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
//             Map<String, String> uriVariables = new HashMap<>();
// uriVariables.put("id", id);
// UriComponents encode = UriComponentsBuilder.newInstance()
//         .scheme("http")
//         .host("localhost")
//         .port(8112)
//         .path("api/v1/employee")
//         .pathSegment("{id}")
//         .buildAndExpand(uriVariables)
//         .encode();
// log.info("encode object: {}", encode);


           //String url = baseUrl + "/{id}";
            ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiDataResponse<Employee>>(){},
                    id);
            if (response.getStatusCode().is2xxSuccessful()) {
                Employee employee = response.getBody().getData();
                log.info("Data of Response Body: {}", employee);
                log.info("HTTP Status Code: {}", response.getStatusCode());
                return employee;
            } else {
                log.warn("Error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception occurred during api client's getEmployeeById method with error: {}", e.getMessage(), e);
        }
        return null;
    }

    public Employee addEmployee(EmployeeInput employeeInput) {
        log.info("Entered ApiClient's addEmployee method with employeeInput: {}.", employeeInput);
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeInput> entity = new HttpEntity<>(employeeInput, headers);

        ResponseEntity<ApiDataResponse<Employee>> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<ApiDataResponse<Employee>>(){});
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Employee creation successful with statusCode: {} and body: {}.", response.getStatusCode(), response.getBody().getData());
            return response.getBody().getData();
        } else {
            log.warn("Error: " + response.getStatusCode());
        }
        return null;
    }

    public String deleteEmployeeById(String id) {
        return null;
    }
}
