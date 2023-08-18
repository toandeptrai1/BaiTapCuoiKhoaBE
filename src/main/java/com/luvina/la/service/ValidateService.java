/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeServiceImpl.java, June 30, 2023 Toannq
 */
package com.luvina.la.service;
import com.luvina.la.exception.EmployeeAddException;
import com.luvina.la.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.luvina.la.config.Constants.*;
import static com.luvina.la.config.Constants.LABEL_EMP_LOGINID;
/**
 * Xử lý validate các thuộc tính của employee
 * @author Toannq
 */
@Service
@RequiredArgsConstructor
public class ValidateService {

    private final EmployeeRepository employeeRepo;

    /**
     * Xử lý validate employeeLoginId
     * @param employeeLoginId cần validate
     */
    public void validateEmployeeLoginId(String employeeLoginId){
        //Throw exception nếu EmployeeLoginId không hợp lệ
        if (employeeLoginId == null) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_LOGINID);
        } else if (employeeLoginId.length() > 50) {
            throw new EmployeeAddException(ER006 + "-" + LABEL_EMP_LOGINID);
        } else if (!employeeLoginId.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new EmployeeAddException(ER019 + "-" + LABEL_EMP_LOGINID);
        } else if (employeeRepo.findByEmployeeLoginId(employeeLoginId).isPresent()) {
            throw new EmployeeAddException(ER003 + "-" + LABEL_EMP_LOGINID);
        }
    }
    /**
     * Xử lý validate employeeName
     * @param employeeName cần validate
     */
    public void validateEmployeeName(String employeeName){
        //Throw exception nếu employeeName không hợp lệ
        if (employeeName == null || employeeName.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_NAME);
        } else if (employeeName.length() > 125) {
            throw new EmployeeAddException(ER006 + "-" + LABEL_EMP_NAME);
        }
    }
    /**
     * Xử lý validate employeeNameKana
     * @param employeeNameKana cần validate
     */
    public void validateEmployeeNameKana(String employeeNameKana){
        //Throw exception nếu EmployeeNameKana không hợp lệ
        if (employeeNameKana == null || employeeNameKana.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_NAMEKANA);
        } else if (employeeNameKana.length() > 125) {
            throw new EmployeeAddException(ER006 + "-" + LABEL_EMP_NAMEKANA);
        } else if (!employeeNameKana.matches("[ぁ-んァ-ン一-龯々〆〤ー・｜｡-ﾟ]+")) {
            throw new EmployeeAddException(ER009 + "-" + LABEL_EMP_NAMEKANA);
        }
    }
    /**
     * Xử lý validate employeeEmail
     * @param employeeEmail cần validate
     */
    public void validateEmployeeEmail(String employeeEmail){
        //Throw exception nếu EmployeeEmail không hợp lệ
        if (employeeEmail == null || employeeEmail.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_EMAIL);
        } else if (employeeEmail.length() > 125) {
            throw new EmployeeAddException(ER006 + "-" + LABEL_EMP_EMAIL);
        }
    }
    /**
     * Xử lý validate employeePhone
     * @param employeePhone cần validate
     */
    public void validateEmployeePhone(String employeePhone){
        //Throw exception nếu employeePhone không hợp lệ
        if (employeePhone == null || employeePhone.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_PHONE);
        } else if (employeePhone.length() > 50) {
            throw new EmployeeAddException(ER006 + "-" + LABEL_EMP_PHONE);
        } else if (!employeePhone.matches("[a-zA-Z0-9!-/:-@\\\\\\[-`{-~]+")) {
            throw new EmployeeAddException(ER008 + "-" + LABEL_EMP_PHONE);
        }
    }
    /**
     * Xử lý validate employeeBirthDate
     * @param employeeBirthDate cần validate
     */
    public void validateEmployeeBirthDate(String employeeBirthDate){
        //Throw exception nếu employeeBirthDate không hợp lệ
        if (employeeBirthDate == null || employeeBirthDate.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_BIRTHDATE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        Date employeeBirdthDate;

        if (!checkDateValid(employeeBirthDate)) {
            throw new EmployeeAddException(ER011 + "-" + LABEL_EMP_BIRTHDATE);
        }
        try {
            employeeBirdthDate = sdf.parse(employeeBirthDate);
        } catch (ParseException e) {
            throw new EmployeeAddException(ER005 + "-" + LABEL_EMP_BIRTHDATE + "-" + "yyyy/MM/dd");
        }
    }
    /**
     * Xử lý validate employeeLoginPassword
     * @param employeeLoginPassword cần validate
     */
    public void validateEmployeeLoginPassword(String employeeLoginPassword){
        //Throw exception nếu employeeLoginPassword không hợp lệ
        if (employeeLoginPassword == null || employeeLoginPassword.equals("")) {
            throw new EmployeeAddException(ER001 + "-" + LABEL_EMP_PASSWORD);
        } else if (employeeLoginPassword.length() > 50 || employeeLoginPassword.length() < 8) {
            throw new EmployeeAddException(ER007 + "-" + LABEL_EMP_PASSWORD + "-8-50");
        }
    }
    /**
     * Xử lý validate departmentId
     * @param departmentId cần validate
     */
    public void validateDepartmentId(String departmentId){
        //Throw exception nếu departmentId không hợp lệ
        if (departmentId == null) {
            throw new EmployeeAddException(ER002 + "-" + LABEL_EMP_DEPARTMENT);
        }
        long departId;
        try {
            departId = Long.parseLong(departmentId);
        } catch (NumberFormatException ex) {
            throw new EmployeeAddException(ER018 + "-" + LABEL_EMP_DEPARTMENT);
        }
        if (departId <= 0) {
            throw new EmployeeAddException(ER018 + "-" + LABEL_EMP_DEPARTMENT);
        }
    }






    /**
     * Phương thức kiểm tra ngày có hợp lệ không
     *
     * @param dateInput chuỗi date đầu vào
     * @return boolean true néu hợp lệ false nếu không hợp lệ
     */
    public Boolean checkDateValid(String dateInput) {
        String[] dateFormats = {"dd/MM/yyyy", "yyyy/MM/dd", "MM/dd/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "MM-dd-yyyy"};
        Date date = null;
        boolean validFormat = false;

        // Lặp qua từng định dạng trong mảng
        for (String format : dateFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            try {
                date = sdf.parse(dateInput);
                validFormat = true;
                break; // Thoát khỏi vòng lặp khi tìm thấy định dạng phù hợp
            } catch (ParseException e) {
                // Không làm gì cả, tiếp tục kiểm tra với định dạng khác trong mảng
            }
        }
        return validFormat;
    }
}
