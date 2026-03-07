package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseModel {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String summary;

    @PositiveOrZero
    @Column(nullable = false)
    private double budget;
}
