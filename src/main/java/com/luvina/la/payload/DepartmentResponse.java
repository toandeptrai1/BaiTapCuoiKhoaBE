/**
 * Copyright(C) 2023 Luvina Software Company
 * DepartmentResponse.java, June 29, 2023 Toannq
 */

package com.luvina.la.payload;
import com.luvina.la.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Chứa các thuộc tính trả về khi gọi api Get departments
 * @author Toannq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Integer code;
    private List<Department> departments;

}
