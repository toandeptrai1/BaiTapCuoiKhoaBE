package com.luvina.la.service;

import com.luvina.la.entity.Certification;
import com.luvina.la.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepo;

    public List<Certification> getAllCertifications(){
        return certificationRepo.findAll();
    }

}
