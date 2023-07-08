/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeService.java, June 30, 2023 Toannq
 */

package com.luvina.la.service;

import com.luvina.la.entity.Employee;
import com.luvina.la.payload.AddEmployeeRequest;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;

/**
 * Interface chứa các xử lý logic liên quan đến Employee
 * @author Toannq
 */
public interface EmployeeService {
    /**
     * Xử lý việc lấy ra danh sách Employee
     * @param employeeRequest Danh sách các RequestParams
     * @param fields danh sách các fields cần sắp xếp
     * @param directions danh sách các thứ tự sắp xếp
     * @return EmployeeResponse
     */
    EmployeeResponse getEmployee(EmployeeRequest employeeRequest,List<String> fields,List<String> directions);

    /**
     * Thực hiện khai báo 1 phương thức xử lý logic add 1 employee
     * @param addEmployeeRequest
     * @return
     */
    Employee addemployee(AddEmployeeRequest addEmployeeRequest);

}
