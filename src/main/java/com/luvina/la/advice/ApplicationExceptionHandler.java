/**
 * Copyright(C) 2023 Luvina Software Company
 *
 * ApplicationExceptionHandler.java, June 29, 2023 Toannq
 */

package com.luvina.la.advice;
import com.luvina.la.exception.OrdValueInvalid;
import com.luvina.la.exception.PageSizeException;
import com.luvina.la.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.*;

/**
 * Tạo ApplicationExceptionHandler
 * controller xử lý ngoại lệ chung cho hệ thống
 * @author Toannq
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
    /**
     * Xử lỗi của Exception OrdValueInvalid
     * @param ex Exception cần xử lý
     * @param request request
     * @return ErrorResponse chứa các thông tin về lỗi
     */
    @ExceptionHandler( OrdValueInvalid.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(OrdValueInvalid ex, WebRequest request){
        Map<String, Object> msg=new HashMap<>();
        if(ex.getMessage().contains("-")){
            String arr[]=ex.getMessage().split("-");
            msg.put("code",arr[0]);
            msg.put("params",arr[1]);

        }else{
            msg.put("code",ex.getMessage());
            msg.put("params",List.of());
        }


        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Xử lỗi của Exception PageSizeException
     * @param ex Exception cần xử lý
     * @param request request
     * @return ErrorResponse chứa các thông tin về lỗi
     */
    @ExceptionHandler(PageSizeException.class)
    public ResponseEntity<ErrorResponse> handlePageSizeException(PageSizeException ex, WebRequest request){
        Map<String, Object> msg=new HashMap<>();
        if(ex.getMessage().contains("-")){
            String arr[]=ex.getMessage().split("-");
            msg.put("code",arr[0]);
            msg.put("params",arr[1]);

        }else{
            msg.put("code",ex.getMessage());
            msg.put("params",List.of());
        }


        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
