/**
 * Copyright(C) 2023 Luvina Software Company
 * Department.java, June 29, 2023 Toannq
 */

package com.luvina.la.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Class chứa các thuộc tính của 1 department
 *
 * @author Toannq
 */
@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "department_name")
    private String departmentName;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<Employee> employees;

}
