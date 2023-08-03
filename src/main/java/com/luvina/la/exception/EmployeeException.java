/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeException.java, June 29, 2023 Toannq
 */

package com.luvina.la.exception;

/**
 * Exception về các employee
 *
 * @author Toannq
 */
public class EmployeeException extends RuntimeException {
    /**
     * Phương thức khởi tạo có tham só của EmployeeException
     *
     * @param message thông tin về lỗi
     */
    public EmployeeException(String message) {
        super(message);
    }
}
