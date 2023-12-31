/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeController.java, June 29, 2023 Toannq
 */

package com.luvina.la.controller;

import com.luvina.la.entity.Employee;
import com.luvina.la.exception.OrdValueInvalid;
import com.luvina.la.exception.PageSizeException;
import com.luvina.la.payload.AddEmployeeRequest;
import com.luvina.la.payload.EmployeeGetByIDResponse;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.luvina.la.config.Constants.*;

/**
 * Tạo EmployeeController
 *
 * @author Toannq
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;

    /**
     * @param employee_name          employeeName nhận được từ request
     * @param department_id          department_id nhận được từ request
     * @param ord_employee_name      ord_employee_name nhận được từ request
     * @param ord_end_date           ord_end_date nhận được từ request
     * @param offset                 offset nhận được từ request
     * @param limit                  limit nhận được từ request
     * @param ord_certification_name ord_certification_name nhận được từ request
     * @param request
     * @return listEmployee danh sách Employee
     */
    @GetMapping("")
    public EmployeeResponse listEmployee(@RequestParam(required = false, defaultValue = "") String employee_name,
                                         @RequestParam(required = false) String department_id,
                                         @RequestParam(required = false, defaultValue = "ASC") String ord_employee_name,
                                         @RequestParam(required = false, defaultValue = "ASC") String ord_end_date,
                                         @RequestParam(required = false, defaultValue = "0") String offset, @RequestParam(required = false, defaultValue = "5") String limit,
                                         @RequestParam(required = false, defaultValue = "ASC") String ord_certification_name
            , HttpServletRequest request) {

        //Xử lý các ngoại lệ
        if (ord_employee_name == null || (!ord_employee_name.equalsIgnoreCase("asc")
                && !ord_employee_name.equalsIgnoreCase("desc"))) {
            throw new OrdValueInvalid(ER021);

        }
        if (ord_certification_name == null || (!ord_certification_name.equalsIgnoreCase("asc")
                && !ord_certification_name.equalsIgnoreCase("desc"))) {
            throw new OrdValueInvalid(ER021);

        }
        if (ord_end_date == null || (!ord_end_date.equalsIgnoreCase("asc")
                && !ord_end_date.equalsIgnoreCase("desc"))) {
            throw new OrdValueInvalid(ER021);

        }
        try {
            if (Integer.parseInt(limit) < 0) {
                throw new PageSizeException(ER018 + "-リミット");
            }

        } catch (NumberFormatException ex) {
            throw new PageSizeException(ER018 + "-リミット");
        }
        try {

            if (Integer.parseInt(offset) < 0) {
                throw new PageSizeException(ER018 + "-オフセット");
            }

        } catch (NumberFormatException ex) {
            throw new PageSizeException(ER018 + "-オフセット");
        }
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
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
     *
     * @param employee Dữ liệu của employee cần thêm
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody AddEmployeeRequest employee) {

        if (employee.getCertifications() == null) {
            employee.setCertifications(new ArrayList<>());
        }
        Employee e = employeeService.addemployee(employee);
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("code", 200);
        apiResponse.put("employeeId", e.getEmployeeId());
        List<String> params = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("code", "MSG001");
        message.put("prams", params);
        apiResponse.put("message", message);

        return ResponseEntity.ok().body(apiResponse);

    }

    /**
     * Xử lý việc gọi phương thức getEmployeeById từ service và
     * trả về api Employee vừa tìm được
     *
     * @param employeeId employeeId cần tìm
     * @return api chứa thông tin employee vùa tìm được
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeGetByIDResponse> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));
    }

    /**
     * Xử lý việc gọi phương thức deleteEmployee từ service và
     * trả về api chứa id của employee vừa xoá
     *
     * @param employeeId employeeId cần xoá
     * @return api chứa id của employee vừa xoá
     */
    @DeleteMapping("/{employeeDelId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name = "employeeDelId") Long employeeId) {
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("code", 200);
        apiResponse.put("employeeId", employeeService.deleteEmployee(employeeId));
        List<String> params = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("code", "MSG003");
        message.put("prams", params);
        apiResponse.put("message", message);
        return ResponseEntity.ok().body(apiResponse);
    }

    /**
     * Xử lý gọi lại phương thức edit Employee từ EmployeeService
     * và trả về api
     *
     * @param employee Dữ liệu của employee cần thêm
     * @return
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editEmployee(@RequestBody AddEmployeeRequest employee) {
        if (employee.getCertifications() == null) {
            employee.setCertifications(new ArrayList<>());
        }
        Employee e = employeeService.editEmployee(employee);
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("code", 200);
        apiResponse.put("employeeId", e.getEmployeeId());
        List<String> params = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("code", "MSG001");
        message.put("prams", params);
        apiResponse.put("message", message);
        return ResponseEntity.ok().body(apiResponse);
    }
}
