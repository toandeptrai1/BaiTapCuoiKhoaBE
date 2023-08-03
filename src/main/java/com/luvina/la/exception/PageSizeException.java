/**
 * Copyright(C) 2023 Luvina Software Company
 * PageSizeException.java, June 29, 2023 Toannq
 */
package com.luvina.la.exception;

/**
 * Exception về Page Size
 *
 * @author Toannq
 */
public class PageSizeException extends RuntimeException {
    /**
     * Khởi tạo PageSizeException với message
     *
     * @param message thông báo về error
     */
    public PageSizeException(String message) {
        super(message);
    }
}
