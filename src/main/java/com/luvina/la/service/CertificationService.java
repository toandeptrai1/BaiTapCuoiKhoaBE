/**
 * Copyright(C) 2023 Luvina Software Company
 * CertificationService.java, June 29, 2023 Toannq
 */
package com.luvina.la.service;

import com.luvina.la.entity.Certification;
import com.luvina.la.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class thực hiện các xử lý logic của Certification
 *
 * @author Toannq
 */
@Service
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepo;

    /**
     * Xử lý việc lấy ra hết danh sách của certification trong csdl
     *
     * @return List<Certification>
     */
    public List<Certification> getAllCertifications() {
        return certificationRepo.findAll();
    }

}
