package com.reliaquest.api.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.ApiListResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiClient {

    // TODO: Handle error 429 with some Retry mechanism
    
    private final RestTemplate restTemplate;
    
    HttpEntity<String> httpEntity;

    String baseUrl = "http://localhost:8112/api/v1/employee";

    public List<Employee> getAll() {
        System.out.println("entered ApiClient's getAll() method...");
        //HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
ResponseEntity<ApiListResponse<List<Employee>>> responseEntity = restTemplate.exchange(
            baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<ApiListResponse<List<Employee>>>(){});
        
        System.out.println("PRINTING RESPONSE ENTITY'S getBody()");
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody().getData();
    }
}