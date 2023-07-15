/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeCertificationReq.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Class chứa các thuộc tính của EmployeeCertificationReq
 * @author Toannq
 */
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
