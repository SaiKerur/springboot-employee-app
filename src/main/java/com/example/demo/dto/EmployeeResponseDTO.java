package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class EmployeeResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Double salary;

    public EmployeeResponseDTO(Long id, String name, String email, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

}
