package com.luvina.la.controller;

import com.luvina.la.entity.Employee;
import com.luvina.la.exception.OrdValueInvalid;
import com.luvina.la.exception.PageSizeException;
import com.luvina.la.payload.AddEmployeeRequest;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;
    @GetMapping("")
    public EmployeeResponse listEmployee(@RequestParam(required = false)  String employee_name, @RequestParam(required = false) String department_id, @RequestParam(required = false) String ord_employee_name,
                                         @RequestParam(required = false) String ord_end_date, @RequestParam String offset, @RequestParam String limit, @RequestParam(required = false) String ord_certification_name
                                         , HttpServletRequest request){


        if(ord_employee_name==null||(!ord_employee_name.equalsIgnoreCase("asc")&&!ord_employee_name.equalsIgnoreCase("desc"))){
            throw new OrdValueInvalid("ERR021");

        }
        if(ord_certification_name==null||(!ord_certification_name.equalsIgnoreCase("asc")&&!ord_certification_name.equalsIgnoreCase("desc"))){
            throw new OrdValueInvalid("ERR021");

        }
        if(ord_end_date==null||(!ord_end_date.equalsIgnoreCase("asc")&&!ord_end_date.equalsIgnoreCase("desc"))){
            throw new OrdValueInvalid("ERR021");

        }
        try{
            if(Integer.parseInt(limit)<0){
                throw new PageSizeException("ERR018-リミット");
            }

        }catch (NumberFormatException ex){
            throw new PageSizeException("ERR018-リミット");
        }
        try{

            if(Integer.parseInt(offset)<0){
                throw new PageSizeException("ERR018-オフセット");
            }

        }catch (NumberFormatException ex){
            throw new PageSizeException("ERR018-オフセット");
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
        return employeeService.getEmployee(employeeRequest);
    }

    /**
     * Xử lý gọi lại phương thức add Employee từ EmployeeService
     * và trả về api
     * @param employee
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody AddEmployeeRequest employee){
        if(employee.getCertifications()==null){
            employee.setCertifications(new ArrayList<>());
        }
        Employee e= employeeService.addemployee(employee);
        Map<String,Object> apiResponse=new HashMap<>();
        apiResponse.put("code",200);
        apiResponse.put("employeeId",e.getEmployeeId());
        List<String> params=new ArrayList<>();
        Map<String,Object> message=new HashMap<>();
        message.put("code","MSG001");
        message.put("prams",params);
        apiResponse.put("message",message);

        return ResponseEntity.ok().body(apiResponse);

    }




}
