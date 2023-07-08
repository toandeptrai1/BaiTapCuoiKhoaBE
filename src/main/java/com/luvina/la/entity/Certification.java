package com.luvina.la.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "certifications")
@Data
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long certificationId;
    @Column(name = "certification_name")
    private String certificationName;
    @Column(name = "certification_level")
    private Integer certificationLevel;
    @OneToMany(mappedBy = "certification",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<EmployeeCertification> EmployeeCertification;


}
