/**
 * Copyright(C) 2023 Luvina Software Company
 * AddEmployeeRequest.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class chứa danh sách param của api Add Employee
 *
 * @author Toannq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEmployeeRequest {

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeLoginId;
    private String employeeTelephone;
    private String employeeBirthDate;
    private String employeeNameKana;
    private String departmentId;
    private String employeeLoginPassword;
    private List<EmployeeCertificationReq> certifications;

}
