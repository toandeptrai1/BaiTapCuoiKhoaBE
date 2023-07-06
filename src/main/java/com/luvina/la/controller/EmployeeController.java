package com.luvina.la.controller;

import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;
    @GetMapping("")
    public EmployeeResponse listEmployee(@RequestParam(required = false)  String employee_name, @RequestParam(required = false) String department_id, @RequestParam(required = false) String ord_employee_name,
                                         @RequestParam(required = false) String ord_end_date, @RequestParam String offset, @RequestParam String limit, @RequestParam(required = false) String ord_certification_name
                                         , HttpServletRequest request){

        Enumeration<String> params = request.getParameterNames();
        List<String> fields=new ArrayList<>();
        List<String> directions=new ArrayList<>();
        while (params.hasMoreElements()){
            String paramName = params.nextElement();
            String paramSort = request.getParameter(paramName);

            switch (paramName){
                case "ord_employee_name": {
                    fields.add("employeeName");
                    directions.add(paramSort);
                    break;
                }
                case "ord_certification_name": {

                    fields.add("employeeCertification.certification.certificationName");
                    directions.add(paramSort);
                    break;
                }
                case "ord_end_date": {
                    fields.add("employeeCertification.endDate");
                    directions.add(paramSort);
                    break;
                }
            }
        }
        EmployeeRequest employeeRequest=EmployeeRequest.builder()
                .employee_name(employee_name)
                .department_id(department_id)
                .ord_employee_name(ord_employee_name)
                .offset(offset)
                .limit(limit)
                .ord_end_date(ord_end_date)
                .ord_certification_name(ord_certification_name)
                .build();
        return employeeService.getEmployee(employeeRequest,fields,directions);
    }
    @GetMapping("/list")
    public EmployeeResponse listEmployees(@RequestParam(required = false)  String employee_name,@RequestParam(required = false) String department_id,@RequestParam(required = false) String ord_employee_name,
                                         @RequestParam(required = false) String ord_end_date,@RequestParam String offset,@RequestParam String limit,@RequestParam(required = false) String ord_certification_name){

        EmployeeRequest employeeRequest=EmployeeRequest.builder()
                .employee_name(employee_name)
                .department_id(department_id)
                .ord_employee_name(ord_employee_name)
                .offset(offset)
                .limit(limit)
                .ord_end_date(ord_end_date)
                .ord_certification_name(ord_certification_name)
                .build();
        return employeeService.getEmployees(employeeRequest);
    }



}
