package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInputModel {

    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;
}
