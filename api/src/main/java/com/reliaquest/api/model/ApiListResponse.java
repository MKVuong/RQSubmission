package com.reliaquest.api.model;

import lombok.Data;

@Data
public class ApiListResponse<T> {

    private T data;
    
}
