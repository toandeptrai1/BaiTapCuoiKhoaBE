package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luvina.la.entity.Certification;
import com.luvina.la.entity.EmployeeCertification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private Date employeeBirthDate;
    private String employeeNameKana;
    private Long departmentId;
    private String employeeLoginPassword;
    private List<EmployeeCertificationReq> certifications;




}
