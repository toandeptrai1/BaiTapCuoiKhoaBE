package com.luvina.la.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private  String employee_name;
    private  String department_id;
    private  String ord_employee_name;
    private  String ord_certification_name;
    private  String ord_end_date;
    private  String offset;
    private  String limit;
}
