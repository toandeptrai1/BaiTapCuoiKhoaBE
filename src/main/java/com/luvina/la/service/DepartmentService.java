package com.luvina.la.service;

import com.luvina.la.entity.Department;
import com.luvina.la.payload.DepartmentResponse;
import com.luvina.la.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepo;
    public DepartmentResponse getDepartment(){
        List<Department> list=departmentRepo.findAll();
        return  DepartmentResponse.builder()
                .code(200)
                .departments(list)
                .build();
    }
}