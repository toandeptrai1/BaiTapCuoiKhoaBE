/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeService.java, June 30, 2023 Toannq
 */

package com.luvina.la.service;

import com.luvina.la.entity.Employee;
import com.luvina.la.payload.AddEmployeeRequest;
import com.luvina.la.payload.EmployeeGetByIDResponse;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;

/**
 * Interface chứa các xử lý logic liên quan đến Employee
 *
 * @author Toannq
 */
public interface EmployeeService {
    /**
     * Xử lý việc lấy ra danh sách Employee
     *
     * @param employeeRequest Danh sách các RequestParams
     * @return EmployeeResponse
     */
    EmployeeResponse getEmployee(EmployeeRequest employeeRequest);

    /**
     * Thực hiện khai báo 1 phương thức xử lý logic add 1 employee
     *
     * @param addEmployeeRequest
     * @return Employee
     */
    Employee addemployee(AddEmployeeRequest addEmployeeRequest);

    /**
     * Thực hiên khai báo 1 phương thức getEmployeeByID
     *
     * @param employeeId employeeId cần tìm
     * @return EmployeeGetByIDResponse
     */
    EmployeeGetByIDResponse getEmployeeById(Long employeeId);

    /**
     * Khai báo phương thức xoá 1 employee theo id của employee
     * @param employeeId id của employee cần xoá
     * @return  employeeId id của employee đã xoá thành công
     */
    Long deleteEmployee(Long employeeId);

}
