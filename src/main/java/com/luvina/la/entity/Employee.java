/**
 * Copyright(C) 2023 Luvina Software Company
 * <p>
 * Employee.java, June 29, 2023 Toannq
 */

package com.luvina.la.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class chứa các thuộc tính của 1 employee
 * @author Toannq
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements Serializable {

    private static final long serialVersionUID = 5771173953267484096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true)
    private Long employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_email")
    private String employeeEmail;

    @Column(name = "employee_login_id")
    private String employeeLoginId;

    @Column(name = "employee_login_password")
    private String employeeLoginPassword;
    @Column(name = "employee_name_kana")
    private String employeeNameKana;
    @Column(name = "employee_birth_date")
    private Date employeeBirthDate;
    @Column(name = "employee_telephone")
    private String employeeTelephone;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EmployeeCertification> employeeCertification;


}
