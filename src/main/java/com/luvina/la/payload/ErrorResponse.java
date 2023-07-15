/**
 * Copyright(C) 2023 Luvina Software Company
 * ErrorResponse.java, June 29, 2023 Toannq
 */
package com.luvina.la.payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Class chứa các thuộc tính về Error
 * @author Toannq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer code;
    private Map<?,?> message;

}
