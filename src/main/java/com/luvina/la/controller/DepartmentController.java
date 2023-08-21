/**
 * Copyright(C) 2023 Luvina Software Company
 * DepartmentController.java, June 29, 2023 Toannq
 */

package com.luvina.la.controller;

import com.luvina.la.payload.DepartmentResponse;
import com.luvina.la.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Tạo DepartmentController có các phương thức
 * trả về api
 *
 * @author Toannq
 */
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    /**
     * Trả vè api list department
     *
     * @return DepartmentResponse
     */
    @GetMapping("")
    public DepartmentResponse getDepartment() {
        return departmentService.getDepartment();
    }
}
