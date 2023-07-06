
package com.luvina.la.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "employees")
@Data
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
    @Column(name="employee_name_kana")
    private String employeeNameKana;
    @Column(name="employee_birth_date")
    private Date employeeBirthDate;
    @Column(name="employee_telephone")
    private String employeeTelephone;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id",referencedColumnName = "department_id")
    private Department department;
    @OneToMany(mappedBy = "employee",fetch = FetchType.EAGER)
    private List<EmployeeCertification> employeeCertification;


}
