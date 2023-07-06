/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeServiceImpl.java, June 30, 2023 Toannq
 */
package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.EmployeeRequest;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    /**
     * Xử lý việc get danh sách employee theo các điều kiện của EmployeeRequest
     *
     * @param employeeRequest chứa các thuộc tính là các điều kiện để thực hiện request
     * @return Trả về 1 EmployeeResponse
     */
    @Override
    public EmployeeResponse getEmployee(EmployeeRequest employeeRequest,List<String> fields,List<String> directions) {
        Pageable pageable;
        Page<Employee> list;


        List<Sort.Order> sortOrders = getSortOrders(fields.toArray(new String[fields.size()]), directions.toArray(new String[directions.size()]));
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

    @Override
    public EmployeeResponse getEmployees(EmployeeRequest employeeRequest) {

        List<Employee> employeeList = employeeRepo.findAll(new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (employeeRequest.getDepartment_id().equals("")) {
                    employeeRequest.setDepartment_id(null);
                }


                if ((employeeRequest.getEmployee_name() != null && employeeRequest.getEmployee_name().equals(""))
                        && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
                ) {

                    predicates.add( criteriaBuilder.equal(root.get("department").get("departmentId"), employeeRequest.getDepartment_id()));
                }  else if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                        && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
                ) {
                    predicates.add(criteriaBuilder.and(
                            criteriaBuilder.like(root.get("employeeName"), "%" + employeeRequest.getEmployee_name() + "%"),
                            criteriaBuilder.equal(root.get("department").get("departmentId"), employeeRequest.getDepartment_id())
                    ));

                }else {
                    predicates.add( criteriaBuilder.like(root.get("employeeName"), "%" + employeeRequest.getEmployee_name() + "%"));
                }



                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(this::mapToEmpDTO).collect(Collectors.toList());

        return EmployeeResponse.builder()
                .code(200)
                .totalRecords(employeeList.size())
                .employees(employeeDTOList)
                .build();
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
