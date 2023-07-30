package com.luvina.la.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
