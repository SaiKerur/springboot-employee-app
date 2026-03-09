package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new LinkedHashSet<>();

    public void addEmployee(Employee employee) {
        if (employees.add(employee)) {
            employee.getProjects().add(this);
        }
    }

    public void removeEmployee(Employee employee) {
        if (employees.remove(employee)) {
            employee.getProjects().remove(this);
        }
    }
}
