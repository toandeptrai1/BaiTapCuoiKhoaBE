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
/**
 * Interface EmployeeRepository kế thừa lại các phương thức của JpaRepository
 * giúp thực hiện các câu truy vấn sql liên quan đến bảng employees
 * @author Toannq
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> , JpaSpecificationExecutor<Employee> {


    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);
    Optional<Employee> findByEmployeeId(Long employeeId);
    /**
     * Xử lý việc tìm 1 employee thỏa mãn departmentID and EmployeeName %like%
     * @param department department cần tìm kiếm
     * @param employeeName employee cần tìm kiếm
     * @param pageable đối tượng pageable khai báo page,size,sort dùng để phân trang
     * @return 1 Page<Employee>
     */
    Page<Employee> findByDepartmentOrEmployeeNameContaining(Department department, String employeeName, Pageable pageable);
    /**
     * Xử lý việc tìm 1 employee thỏa mãn departmentID or EmployeeName %like%
     * @param department department cần tìm kiếm
     * @param employeeName employee cần tìm kiếm
     * @param pageable đối tượng pageable khai báo page,size,sort dùng để phân trang
     * @return 1 Page<Employee>
     */
    Page<Employee> findByDepartmentAndEmployeeNameContaining(Department department,String employeeName,Pageable pageable);


}
