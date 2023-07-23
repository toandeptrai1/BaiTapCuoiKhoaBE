package com.luvina.la.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 6868189362900231672L;

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeLoginId;
    private String departmentName;
    private String employeeTelephone;
    private String certificationName;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date employeeBirthDate;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date endDate;
    private Long score;
}
