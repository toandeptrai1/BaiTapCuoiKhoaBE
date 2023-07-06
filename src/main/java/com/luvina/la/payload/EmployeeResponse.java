package com.luvina.la.payload;

import com.luvina.la.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Integer code;
    private Integer totalRecords;
    private List<EmployeeDTO> employees;

}
