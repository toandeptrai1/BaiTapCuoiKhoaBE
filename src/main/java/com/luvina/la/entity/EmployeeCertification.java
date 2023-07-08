package com.luvina.la.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employees_certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_certification_id.certification.")
    private Long employeeCertificationId;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "certification_id")
    private Certification certification;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
    @Column(name = "score")
    private Float score;

}
