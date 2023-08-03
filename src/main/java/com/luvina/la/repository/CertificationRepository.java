/**
 * Copyright(C) 2023 Luvina Software Company
 * CertificationRepository.java, June 29, 2023 Toannq
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface CertificationRepository kế thừa lại các phương thức của JpaRepository
 * giúp thực hiện các câu truy vấn sql liên quan đến bảng certification
 *
 * @author Toannq
 */
public interface CertificationRepository extends JpaRepository<Certification, Long> {
}
