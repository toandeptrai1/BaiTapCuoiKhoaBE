/**
 * Copyright(C) 2023 Luvina Software Company
 * DepartmentService.java, June 29, 2023 Toannq
 */

package com.luvina.la.service;

import com.luvina.la.entity.Department;
import com.luvina.la.payload.DepartmentResponse;
import com.luvina.la.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class thực hiện khai báo các phương thức xử lý logic của Department
 *
 * @author Toannq
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepo;

    public DepartmentResponse getDepartment() {
        List<Department> list = departmentRepo.findAll();
        return DepartmentResponse.builder()
                .code(200)
                .departments(list)
                .build();
    }
}