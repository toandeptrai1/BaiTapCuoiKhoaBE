/**
 * Copyright(C) 2023 Luvina Software Company
 *
 * ApplicationExceptionHandler.java, June 29, 2023 Toannq
 */

package com.luvina.la.advice;
import com.luvina.la.exception.EmployeeAddException;
import com.luvina.la.exception.OrdValueInvalid;
import com.luvina.la.exception.PageSizeException;
import com.luvina.la.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    /**
     * Xử lý ngoại lệ của EmployeeAddException
     * @param ex Đối tượng loi EmployeeAddException
     * @param request request
     * @return api ErrorResponse chứa các thông tin về lỗi
     */
    @ExceptionHandler(EmployeeAddException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAddException(EmployeeAddException ex, WebRequest request){
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
     * Xử lý ngoại lệ về ép kiểu JSON
     * @param ex Đối tượng thông tin vè lỗi
     * @param request request
     * @return ErrorResponse đối tượng chứa thông tin về lỗi
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleDateException(HttpMessageNotReadableException ex, WebRequest request){
        String errorMessage=ex.getMessage();
        int startIndex = errorMessage.indexOf("[\"") + 2;
        int endIndex = errorMessage.indexOf("\"]", startIndex);

        // Lấy chuỗi trường từ thông báo lỗi
        String fieldName = errorMessage.substring(startIndex, endIndex);
        Map<String, Object> msg=new HashMap<>();
        if(fieldName.equals("employeeBirthDate")){
            msg.put("code","ER011");
            msg.put("params","生年月日");

        } else if (fieldName.equals("departmentId")) {
            msg.put("code","ER0018");
            msg.put("params","グループ");
        } else  {
            msg.put("code",fieldName);
            msg.put("params",List.of());
        }
        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
