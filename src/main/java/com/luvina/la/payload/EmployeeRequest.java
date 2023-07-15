/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeRequest.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Class khai báo các parameters của employee
 * @author Toannq
 */
public class EmployeeRequest {
    private  String employee_name;
    private  String department_id;
    private  String ord_employee_name;
    private  String ord_certification_name;
    private  String ord_end_date;
    private  String offset;
    private  String limit;
}
