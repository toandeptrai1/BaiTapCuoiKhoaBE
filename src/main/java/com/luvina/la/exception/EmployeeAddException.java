/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeException.java, July 19, 2023 Toannq
 */
package com.luvina.la.exception;

/**
 * Lớp khai báo 1 ngoại lệ nếu api addEmployee lỗi
 * @author Toannq
 */
public class EmployeeAddException extends RuntimeException{
    /**
     * Phương thức khởi tạo có tham số của EmployeeAddException
     * @param message message về lỗi
     */
    public EmployeeAddException(String message) {
        super(message);
    }
}
