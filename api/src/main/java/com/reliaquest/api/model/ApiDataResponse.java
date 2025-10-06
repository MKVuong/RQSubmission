package com.reliaquest.api.model;

import lombok.Data;

@Data
public class ApiDataResponse<T> {

    private T data;
}
