package com.luvina.la.controller;

import com.luvina.la.payload.CertificationResponse;
import com.luvina.la.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("certification")
public class CertificationController {
    private final CertificationService certificationService;
    @GetMapping("")
    public ResponseEntity<CertificationResponse> getAll(){
        CertificationResponse certificationResponse=CertificationResponse.builder()
                .code(200)
                .certifications(certificationService.getAllCertifications())
                .build();
        return ResponseEntity.ok().body(certificationResponse);
    }

}
