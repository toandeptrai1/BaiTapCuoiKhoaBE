package com.luvina.la.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCertificationReq {
    private Long certificationId;
    private Date certificationStartDate;
    private Date certificationEndDate;
    private Float employeeCertificationScore;
}
