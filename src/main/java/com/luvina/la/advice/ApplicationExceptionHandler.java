package com.luvina.la.advice;

import com.luvina.la.exception.OrdValueInvalid;
import com.luvina.la.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler( OrdValueInvalid.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(OrdValueInvalid ex, WebRequest request){
        Map<String, Object> msg=new HashMap<>();

       msg.put("code",ex.getMessage());
       msg.put("params",List.of());

        ErrorResponse errorResponse=ErrorResponse.builder().code(500).message(msg).build();

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
