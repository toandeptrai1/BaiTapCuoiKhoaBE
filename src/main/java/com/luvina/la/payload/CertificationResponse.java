/**
 * Copyright(C) 2023 Luvina Software Company
 * CertificationResponse.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;

import com.luvina.la.entity.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chứa các thuộc tính trả về khi gọi api lấy danh sách certification
 *
 * @author Toannq
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationResponse {
    private Integer code;
    private List<Certification> certifications;

}
