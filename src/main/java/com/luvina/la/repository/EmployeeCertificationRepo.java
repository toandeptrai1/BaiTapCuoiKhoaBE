/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeCertificationRepo.java, June 29, 2023 Toannq
 */
package com.luvina.la.repository;
import com.luvina.la.entity.EmployeeCertification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface EmployeeCertificationRepo kế thừa JpaRepository
 * chứa các phương thức có thể tương tác với csdl
 * @author Toannq
 */
public interface EmployeeCertificationRepo extends JpaRepository<EmployeeCertification,Long> {

}
