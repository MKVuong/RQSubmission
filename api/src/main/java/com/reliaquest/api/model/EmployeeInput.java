package com.reliaquest.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInput {

    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @Min(value = 1, message = "Salary must be positive")
    private Integer salary;

    @Min(value = 16, message = "Age must be 16 or over")
    @Max(value = 75, message = "Age must be 75 or under")
    private Integer age;

    @NotBlank(message = "Title is mandatory")
    private String title;
}
