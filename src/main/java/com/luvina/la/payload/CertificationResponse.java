package com.luvina.la.payload;

import com.luvina.la.entity.Certification;
import com.luvina.la.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationResponse {
    private Integer code;
    private List<Certification> certifications;

}
