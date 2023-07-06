package com.luvina.la.repository;

import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> , JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);
    Optional<Employee> findByEmployeeId(Long employeeId);
    Page<Employee> findByDepartmentOrEmployeeNameContaining(Department department, String employeeName, Pageable pageable);
    Page<Employee> findByDepartmentAndEmployeeNameContaining(Department department,String employeeName,Pageable pageable);


}
