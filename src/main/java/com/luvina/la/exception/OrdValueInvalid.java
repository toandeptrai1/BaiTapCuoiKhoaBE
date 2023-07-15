/**
 * Copyright(C) 2023 Luvina Software Company
 * OrdValueInvalid.java, June 29, 2023 Toannq
 */
package com.luvina.la.exception;

/**
 * Exception về thứ tự sắp xếp
 * @author Toannq
 */
public class OrdValueInvalid extends RuntimeException{
    /**
     * Phưg thức khởi tạo có tham số OrdValueInvalid
     * @param message thông báo về lỗi
     */
    public OrdValueInvalid(String message) {
        super(message);

    }
}
