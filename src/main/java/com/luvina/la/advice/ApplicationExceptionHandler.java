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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
            msg.put("params",List.of(arr[1]));

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
            msg.put("params",List.of(arr[1]));

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
            if(ex.getMessage().contains("ER007-パスワード")){
                msg.put("code",arr[0]);
                msg.put("params",List.of(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3])));
            } else if (ex.getMessage().contains("ER005-資格交付日")||ex.getMessage().contains("ER005-失効日")
                    ||ex.getMessage().contains("ER005-生年月日")) {
                msg.put("code",arr[0]);
                msg.put("params",List.of(arr[1],arr[2]));
            } else{
                msg.put("code",arr[0]);
                msg.put("params",List.of(arr[1]));
            }


        }else{
            msg.put("code",ex.getMessage());
            msg.put("params",List.of());
        }


        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *Xử lý ngoại lệ khi khi thực hiện getEmployeeById,deleleEmployee
     * @param ex Đối tượng error
     * @return api chứa thông tin lỗi
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleGetEmployeeException(MissingPathVariableException ex){

        Map<String, Object> msg=new HashMap<>();

        msg.put("code","ER001");
        msg.put("params",List.of("ID"));
        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     *Xử lý ngoại lệ khi khi thực hiện getEmployeeById,deleleEmployee
     * @param ex Đối tượng error
     * @return api chứa thông tin lỗi
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleGetEmployeeException(MethodArgumentTypeMismatchException ex){

        Map<String, Object> msg=new HashMap<>();
        if(ex.getName().equals("employeeId")){
            msg.put("code","ER013");
            msg.put("params",List.of("ID"));
        }else {
            msg.put("code","ER014");
            msg.put("params",List.of("ID"));
        }
        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *Xử lý ngoại lệ khi khi thực hiện ,deleleEmployee
     * @param ex Đối tượng error
     * @return api chứa thông tin lỗi
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleGetEmployeeException(HttpRequestMethodNotSupportedException ex){

        Map<String, Object> msg=new HashMap<>();

        msg.put("code","ER001");
        msg.put("params",List.of("ID"));
        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
