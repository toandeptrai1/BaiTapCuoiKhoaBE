/**
 * Copyright(C) 2023 Luvina Software Company
 *
 * MessageSample.java, June 29, 2023 Toannq
 */
package com.luvina.la.repository;
import com.luvina.la.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Interface DepartmentRepository kế thừa lại các phương thức của JpaRepository
 * giúp thực hiện các câu truy vấn sql liên quan đến bảng departments
 *
 * @author Toannq
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
