package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luvina.la.dto.EmployeeCertificationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeGetByIDResponse {
    private Long code;
    private Long employeeId;
    private String employeeName;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date employeeBirthDate;
    private Long departmentId;
    private String departmentName;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeNameKana;
    private String employeeLoginId;
    private List<EmployeeCertificationDTO> certifications;
}
