/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeServiceImpl.java, June 30, 2023 Toannq
 */
package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Certification;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import com.luvina.la.payload.AddEmployeeRequest;
import com.luvina.la.payload.EmployeeCertificationReq;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepo;
import com.luvina.la.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Xử lý gọi lại các phương thức của repository và xử lý phần logic liên quan đến
 * lớp Employee
 *
 * @author Toannq
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final CertificationRepository certificationRepo;
    private final EmployeeCertificationRepo employeeCertificationRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Xử lý việc get danh sách employee theo các điều kiện của EmployeeRequest
     *
     * @param employeeRequest chứa các thuộc tính là các điều kiện để thực hiện request

     * @return Trả về 1 EmployeeResponse
     */
    @Override
    public EmployeeResponse getEmployee(EmployeeRequest employeeRequest) {
        Pageable pageable;
        Page<Employee> list;
        String[] fields = {"employeeName", "employeeCertification.certification.certificationName", "employeeCertification.endDate"};
        String[] directions = {employeeRequest.getOrd_employee_name(), employeeRequest.getOrd_certification_name(), employeeRequest.getOrd_end_date()};



        List<Sort.Order> sortOrders = getSortOrders(fields, directions);
        if (!sortOrders.isEmpty()) {
            Sort sort = Sort.by(sortOrders);
            pageable=PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),Integer.parseInt(employeeRequest.getLimit()),sort);


        } else {
            pageable=PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),Integer.parseInt(employeeRequest.getLimit()),Sort.by("employeeId"));

        }
        if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
        ) {
            Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id())).orElseThrow();
            list = employeeRepo.findByDepartmentAndEmployeeNameContaining(department, employeeRequest.getEmployee_name(),pageable);
        } else if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                || (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))) {
            if (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals("")) {
                Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id())).orElseThrow();
                list = employeeRepo.findByDepartmentOrEmployeeNameContaining(department, null,pageable);
            } else {
                list = employeeRepo.findByDepartmentOrEmployeeNameContaining(null, employeeRequest.getEmployee_name(),pageable);
            }

        } else {
            list = employeeRepo.findAll(pageable);
        }

        List<EmployeeDTO> employeeDTOList = list.stream().map(this::mapToEmpDTO).collect(Collectors.toList());

        return EmployeeResponse.builder()
                .code(200)
                .totalRecords((int) list.getTotalElements())
                .employees(employeeDTOList)
                .build();
    }

    /**
     * Xử lý logic cho việc add 1 employee vào bảng employee
     * @param addEmployeeRequest
     * @return employee
     */
    @Transactional
    @Override
    public Employee addemployee(AddEmployeeRequest addEmployeeRequest) {
        addEmployeeRequest.setEmployeeLoginPassword(passwordEncoder.encode(addEmployeeRequest.getEmployeeLoginPassword()));
        Department department=departmentRepo.findById(addEmployeeRequest.getDepartmentId()).orElseThrow();
        Employee employee=mapToAddemployeeRequestToEmployee(addEmployeeRequest,department);
        Employee addEmployee=employeeRepo.save(employee);
        List<EmployeeCertification> employeeCertificationList=new ArrayList<>();

        if(addEmployeeRequest.getCertifications().size()>0){
            for (EmployeeCertificationReq e: addEmployeeRequest.getCertifications()) {
                EmployeeCertification employeeCertification=new EmployeeCertification();
                Certification certification=new Certification();
                certification=certificationRepo.findById(e.getCertificationId()).orElseThrow();
                employeeCertification=mapEmployeeCertificationReqToEmCertificate(e,certification,addEmployee);
                employeeCertificationList.add(employeeCertification);
            }
        }
        if(employeeCertificationList.size()>0){
            employeeCertificationList.forEach(employeeCertificationRepo::save);
        }

        return addEmployee;
    }


    /**
     * Xử lý việc map từ 1 Employee sang EmployeeDTO
     *
     * @param employee Thông tin của 1 Employee
     * @return Trả về 1 EmployeeDTO
     */
    public EmployeeDTO mapToEmpDTO(Employee employee) {

        EmployeeDTO.EmployeeDTOBuilder builder = EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeEmail(employee.getEmployeeEmail())
                .employeeTelephone(employee.getEmployeeTelephone())
                .employeeLoginId(employee.getEmployeeLoginId())
                .departmentName(employee.getDepartment().getDepartmentName())
                .employeeName(employee.getEmployeeName())
                .employeeBirthDate(employee.getEmployeeBirthDate());


        List<EmployeeCertification> employeeCertifications = employee.getEmployeeCertification();
        if (!employeeCertifications.isEmpty()) {
            EmployeeCertification firstCertification = employeeCertifications.get(0);
            builder.certificationName(firstCertification.getCertification().getCertificationName())
                    .score(firstCertification.getScore())
                    .endDate(firstCertification.getEndDate());
        }


        return builder.build();
    }

    /**
     * Xử lý việc chuyển từ AddEmployeeRequest thành 1 Employee
     * @param addEmployeeRequest
     * @param department
     * @return Employee
     */
    public Employee mapToAddemployeeRequestToEmployee(AddEmployeeRequest addEmployeeRequest,Department department){
        return Employee.builder()
                .employeeName(addEmployeeRequest.getEmployeeName())
                .employeeId(addEmployeeRequest.getEmployeeId())
                .employeeTelephone(addEmployeeRequest.getEmployeeTelephone())
                .employeeEmail(addEmployeeRequest.getEmployeeEmail())
                .employeeNameKana(addEmployeeRequest.getEmployeeNameKana())
                .employeeLoginPassword(addEmployeeRequest.getEmployeeLoginPassword())
                .employeeLoginId(addEmployeeRequest.getEmployeeLoginId())
                .employeeBirthDate(addEmployeeRequest.getEmployeeBirthDate())
                .department(department)
                .build();
    }

    /**
     * Xử lý việc chuyển 1 EmployeeCertificationReq thành 1 EmployeeCertification
     * @param req EmployeeCertificationReq cần chuyển đổi
     * @param certification certification chứng chỉ tiếng nhật
     * @param employee employee
     * @return EmployeeCertification
     */
    public EmployeeCertification mapEmployeeCertificationReqToEmCertificate(EmployeeCertificationReq req, Certification certification,Employee employee){
        return EmployeeCertification.builder()
                .certification(certification)
                .startDate(req.getCertificationStartDate())
                .endDate(req.getCertificationEndDate())
                .score(req.getEmployeeCertificationScore())
                .employee(employee)
                .build();
    }
    /**
     * Xử lý việc chuyển đổi fields,directions thành các Sort.Order tương ứng
     *
     * @param fields     danh sách các fields cần sắp xếp
     * @param directions danh sách các thứ tự sắp xếp
     * @return Danh sách Sort.Order
     */
    public  List<Sort.Order> getSortOrders(String[] fields, String[] directions) {
        List<Sort.Order> sortOrders = new ArrayList<>();

        if (fields == null || directions == null) {
            return sortOrders;
        }

        int length = Math.min(fields.length, directions.length);
        for (int i = 0; i < length; i++) {
            String field = fields[i];
            String direction = directions[i];

            if (field != null && !field.isEmpty() && direction != null && !direction.isEmpty()) {
                Sort.Direction sortDirection = Sort.Direction.ASC;
                if (direction.equalsIgnoreCase("desc")) {
                    sortDirection = Sort.Direction.DESC;
                }
                sortOrders.add(new Sort.Order(sortDirection, field));
            }
        }

        return sortOrders;
    }

}
