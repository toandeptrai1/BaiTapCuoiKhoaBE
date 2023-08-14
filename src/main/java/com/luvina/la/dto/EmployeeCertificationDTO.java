/*
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeCertificationDTO.java, July 30, 2023 Toannq
 */
package com.luvina.la.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Class chứa các thông tin của 1 EmployeeCertificationDTO
 *
 * @author ToanNQ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCertificationDTO {
    private Long certificationId;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date certificationStartDate;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date certificationEndDate;
    private Long employeeCertificationScore;


}
