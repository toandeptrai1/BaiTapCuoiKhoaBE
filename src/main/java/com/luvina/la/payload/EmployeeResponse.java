/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeResponse.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;
import com.luvina.la.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * class chứa các thuộc tính sẽ được tra về khi
 * goi api getEmployee
 * @author Toannq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Integer code;
    private Integer totalRecords;
    private List<EmployeeDTO> employees;

}
