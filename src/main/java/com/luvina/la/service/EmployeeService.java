/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeService.java, June 30, 2023 Toannq
 */

package com.luvina.la.service;

import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;

/**
 * Interface chứa các method liên quan đến Employee
 * @author Toannq
 */
public interface EmployeeService {
    /**
     * Xử lý việc get danh sách employee theo các die
     * @param employeeRequest
     * @return
     */
    EmployeeResponse getEmployee(EmployeeRequest employeeRequest,List<String> fields,List<String> directions);
    EmployeeResponse getEmployees(EmployeeRequest employeeRequest);
}
