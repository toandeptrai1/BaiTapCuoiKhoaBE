/**
 * Copyright(C) 2023 Luvina Software Company
 *
 * CertificationController.java, June 29, 2023 Toannq
 */

package com.luvina.la.controller;
import com.luvina.la.payload.CertificationResponse;
import com.luvina.la.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tạo CertificationController chứa các phương thức
 * trả về api liên quan đến certification
 * @author Toannq
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("certification")
public class CertificationController {
    private final CertificationService certificationService;

    /**
     * Xử lý việc trả về api get danh sách certification
     * @return danh sách cetification
     */
    @GetMapping("")
    public ResponseEntity<CertificationResponse> getAll(){
        CertificationResponse certificationResponse=CertificationResponse.builder()
                .code(200)
                .certifications(certificationService.getAllCertifications())
                .build();
        return ResponseEntity.ok().body(certificationResponse);
    }

}
