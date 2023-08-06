/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeServiceImpl.java, June 30, 2023 Toannq
 */
package com.luvina.la.service;

import com.luvina.la.dto.EmployeeCertificationDTO;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Certification;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import com.luvina.la.exception.EmployeeAddException;
import com.luvina.la.payload.*;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepo;
import com.luvina.la.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
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
        String[] fields = {"employeeName", "employeeCertification.certification.certificationName",
                "employeeCertification.endDate"};
        String[] directions = {employeeRequest.getOrd_employee_name(), employeeRequest.getOrd_certification_name(),
                employeeRequest.getOrd_end_date()};
        List<Sort.Order> sortOrders = getSortOrders(fields, directions);
        if (!sortOrders.isEmpty()) {
            Sort sort = Sort.by(sortOrders);
            pageable = PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),
                    Integer.parseInt(employeeRequest.getLimit()), sort);


        } else {
            pageable = PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),
                    Integer.parseInt(employeeRequest.getLimit()), Sort.by("employeeId"));

        }
        if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
        ) {
            Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                    .orElseThrow();
            list = employeeRepo.findByDepartmentAndEmployeeNameContaining(department, employeeRequest.getEmployee_name()
                    , pageable);
        } else if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                || (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))) {
            if (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals("")) {
                Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                        .orElseThrow();
                list = employeeRepo.findByDepartmentOrEmployeeNameContaining(department, null, pageable);
            } else {
                list = employeeRepo.findByDepartmentOrEmployeeNameContaining(null,
                        employeeRequest.getEmployee_name(), pageable);
            }

        } else {
            list = employeeRepo.findAll(pageable);
        }


        List<EmployeeDTO> employeeDTOList = list.getContent().stream().map(this::mapToEmpDTO).collect(Collectors.toList());

        return EmployeeResponse.builder()
                .code(200)
                .totalRecords((int) list.getTotalElements())
                .employees(employeeDTOList)
                .build();
    }

    /**
     * Xử lý logic cho việc add 1 employee vào bảng employee
     *
     * @param addEmployeeRequest
     * @return employee
     */
    @Transactional
    @Override
    public Employee addemployee(AddEmployeeRequest addEmployeeRequest) {
        //Throw exception nếu EmployeeLoginId không hợp lệ
        if (addEmployeeRequest == null) {
            throw new EmployeeAddException("ER001-アカウント名");
        } else if (addEmployeeRequest.getEmployeeLoginId().length() > 50) {
            throw new EmployeeAddException("ER006-アカウント名");
        } else if (!addEmployeeRequest.getEmployeeLoginId().matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new EmployeeAddException("ER019-アカウント名");
        } else if (employeeRepo.findByEmployeeLoginId(addEmployeeRequest.getEmployeeLoginId()).isPresent()) {
            throw new EmployeeAddException("ER003-アカウント名");
        }

        //Throw exception nếu employeeName không hợp lệ
        if (addEmployeeRequest.getEmployeeName() == null || addEmployeeRequest.getEmployeeName().equals("")) {
            throw new EmployeeAddException("ER001-氏名");
        } else if (addEmployeeRequest.getEmployeeName().length() > 125) {
            throw new EmployeeAddException("ER006-氏名");
        }

        //Throw exception nếu employeeNameKana không hợp lệ
        if (addEmployeeRequest.getEmployeeNameKana() == null || addEmployeeRequest.getEmployeeNameKana().equals("")) {
            throw new EmployeeAddException("ER001-カタカナ氏名");
        } else if (addEmployeeRequest.getEmployeeNameKana().length() > 125) {
            throw new EmployeeAddException("ER006-カタカナ氏名");
        } else if (!addEmployeeRequest.getEmployeeNameKana().matches("[ぁ-んァ-ン一-龯々〆〤ー・｜｡-ﾟ]+")) {
            throw new EmployeeAddException("ER009-カタカナ氏名");
        }

        //Throw exception nếu employeeBirthDate không hợp lệ
        if (addEmployeeRequest.getEmployeeBirthDate() == null || addEmployeeRequest.getEmployeeBirthDate().equals("")) {
            throw new EmployeeAddException("ER001-カタカナ氏名");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        Date employeeBirdthDate;

        if (!checkDateValid(addEmployeeRequest.getEmployeeBirthDate())) {
            throw new EmployeeAddException("ER011-生年月日");
        }
        try {
            employeeBirdthDate = sdf.parse(addEmployeeRequest.getEmployeeBirthDate());
        } catch (ParseException e) {
            throw new EmployeeAddException("ER005-生年月日-yyyy/MM/dd");
        }


        //Throw exception nếu employeeEmail không hợp lệ
        if (addEmployeeRequest.getEmployeeEmail() == null || addEmployeeRequest.getEmployeeEmail().equals("")) {
            throw new EmployeeAddException("ER001-メールアドレス");
        } else if (addEmployeeRequest.getEmployeeEmail().length() > 125) {
            throw new EmployeeAddException("ER006-メールアドレス");
        }
        //Throw exception nếu employeeTelephone không hợp lệ
        if (addEmployeeRequest.getEmployeeTelephone() == null || addEmployeeRequest.getEmployeeTelephone().equals("")) {
            throw new EmployeeAddException("ER001-電話番号");
        } else if (addEmployeeRequest.getEmployeeTelephone().length() > 50) {
            throw new EmployeeAddException("ER006-電話番号");
        } else if (!addEmployeeRequest.getEmployeeTelephone().matches("[a-zA-Z0-9!-/:-@\\\\\\[-`{-~]+")) {
            throw new EmployeeAddException("ER008-電話番号");
        }

        //Throw exception nếu employeeLoginPassword không hợp lệ
        if (addEmployeeRequest.getEmployeeLoginPassword() == null || addEmployeeRequest.getEmployeeLoginPassword()
                .equals("")) {
            throw new EmployeeAddException("ER001-パスワード");
        } else if (addEmployeeRequest.getEmployeeLoginPassword().length() > 50
                || addEmployeeRequest.getEmployeeLoginPassword().length() < 8) {
            throw new EmployeeAddException("ER007-パスワード-8-50");
        }
        //Throw exception nếu departmentId không hợp lệ

        if (addEmployeeRequest.getDepartmentId() == null) {
            throw new EmployeeAddException("ER002-グループ");
        }
        Long departId;
        try {
            departId = Long.parseLong(addEmployeeRequest.getDepartmentId());
        } catch (NumberFormatException ex) {
            throw new EmployeeAddException("ER0018-グループ");
        }
        if (departId <= 0) {
            throw new EmployeeAddException("ER0018-グループ");
        }

        //Throw exception nếu Certifications không hợp lệ
        if (addEmployeeRequest.getCertifications().size() > 0) {
            addEmployeeRequest.getCertifications().forEach(cer -> {
                //Throw exception nếu CertificationStartDate không hợp lệ
                if (cer.getCertificationStartDate() == null || cer.getCertificationStartDate().equals("")) {
                    throw new EmployeeAddException("ER001-資格交付日");
                }
                Date endDate;
                Date startDate;
                if (!checkDateValid(cer.getCertificationStartDate())) {
                    throw new EmployeeAddException("ER011-資格交付日");
                }
                try {
                    startDate = sdf.parse(cer.getCertificationStartDate());
                } catch (ParseException e) {
                    throw new EmployeeAddException("ER005-資格交付日-yyyy/MM/dd");
                }
                ///Throw exception nếu CertificationEndDate không hợp lệ
                if (cer.getCertificationEndDate() == null || cer.getCertificationEndDate().equals("")) {
                    throw new EmployeeAddException("ER001-失効日");
                }
                if (!checkDateValid(cer.getCertificationEndDate())) {
                    throw new EmployeeAddException("ER011-失効日");
                }
                try {
                    endDate = sdf.parse(cer.getCertificationEndDate());
                    if (endDate.before(startDate) || endDate.equals(startDate)) {
                        throw new EmployeeAddException("ER012");
                    }
                } catch (ParseException e) {
                    throw new EmployeeAddException("ER005-失効日-yyyy/MM/dd");

                }

                ///Throw exception nếu EmployeeCertificationScore không hợp lệ
                if (cer.getEmployeeCertificationScore() == null || cer.getEmployeeCertificationScore().equals("")) {
                    throw new EmployeeAddException("ER001-点数");
                }
                long score;
                try {
                    score = Long.parseLong(cer.getEmployeeCertificationScore());
                    if (score <= 0) {
                        throw new EmployeeAddException("ER018-点数");
                    }
                } catch (NumberFormatException ex) {
                    throw new EmployeeAddException("ER018-点数");
                }
                //certificationId
                if (cer.getCertificationId() == null || cer.getCertificationId().equals("")) {
                    throw new EmployeeAddException("ER001-資格");
                }
                long certiId;
                try {
                    certiId = Long.parseLong(cer.getCertificationId());
                    if (certiId <= 0) {
                        throw new EmployeeAddException("ER018-資格");
                    }
                } catch (NumberFormatException ex) {
                    throw new EmployeeAddException("ER018-資格");
                }
                if (!certificationRepo.existsById(Long.parseLong(cer.getCertificationId()))) {
                    throw new EmployeeAddException("ER004-資格");
                }

            });
        }

        //mã hoá password
        addEmployeeRequest.setEmployeeLoginPassword(passwordEncoder.encode(addEmployeeRequest.getEmployeeLoginPassword()
        ));
        Department department = departmentRepo.findById(Long.parseLong(addEmployeeRequest.getDepartmentId()))
                .orElseThrow(() -> new EmployeeAddException("ER004-グループ"));
        Employee employee = mapToAddemployeeRequestToEmployee(addEmployeeRequest, department);
        Employee addEmployee = employeeRepo.save(employee);
        List<EmployeeCertification> employeeCertificationList = new ArrayList<>();
        //kiểm tra xem employee có certification không
        if (addEmployeeRequest.getCertifications().size() > 0) {
            for (EmployeeCertificationReq e : addEmployeeRequest.getCertifications()) {
                EmployeeCertification employeeCertification = new EmployeeCertification();
                Certification certification = new Certification();
                certification = certificationRepo.findById(Long.parseLong(e.getCertificationId())).orElseThrow();
                employeeCertification = mapEmployeeCertificationReqToEmCertificate(e, certification, addEmployee);
                employeeCertificationList.add(employeeCertification);
            }
        }
        //Lưu các certification
        if (employeeCertificationList.size() > 0) {
            employeeCertificationList.forEach(employeeCertificationRepo::save);
        }

        return addEmployee;
    }

    /**
     * Xử lý việc get EmployeeById
     *
     * @param employeeId employeeId cần tìm
     * @return EmployeeGetByIDResponse chứa thông tin employee tìm dược
     */
    @Override
    public EmployeeGetByIDResponse getEmployeeById(Long employeeId) {
        //Get employee từ db nếu không có sẽ throw 1 exception
        Employee employee = employeeRepo.findByEmployeeId(employeeId).orElseThrow(() ->
                new EmployeeAddException("ER013-ID"));
        EmployeeGetByIDResponse employeeGetByIDResponse = EmployeeGetByIDResponse.builder()
                .code(200L)
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getEmployeeName())
                .employeeTelephone(employee.getEmployeeTelephone())
                .employeeEmail(employee.getEmployeeEmail())
                .employeeBirthDate(employee.getEmployeeBirthDate())
                .employeeLoginId(employee.getEmployeeLoginId())
                .employeeNameKana(employee.getEmployeeNameKana())
                .departmentId(employee.getDepartment().getDepartmentId())
                .departmentName(employee.getDepartment().getDepartmentName())
                .build();
        //Kiểm tra xem có certification không
        if (employee.getEmployeeCertification().size() > 0) {

            employeeGetByIDResponse.setCertifications(employee.getEmployeeCertification().stream()
                    .sorted(Comparator.comparing(cer->
                            cer.getCertification().getCertificationLevel())).toList().stream()
                    .map(this::mapToEmployeeCertificationDTO).collect(Collectors.toList()));
        } else {
            employeeGetByIDResponse.setCertifications(List.of());
        }
        return employeeGetByIDResponse;
    }

    /**
     * Xử lý việc xoá 1 employee theo id của employee
     * @param employeeId id của employee cần xoá
     * @return employeeId id của employee
     */
    @Override
    public Long deleteEmployee(Long employeeId) {
        //Kiểm tra xem có tồn tại employee có id này trong db không
        Employee emp=employeeRepo.findByEmployeeId(employeeId).orElseThrow(()->new EmployeeAddException("ER014-ID"));
        employeeRepo.delete(emp);
        return employeeId;
    }


    /**
     * Xử lý việc map từ 1 Employee sang EmployeeDTO
     *
     * @param employee Thông tin của 1 Employee
     * @return Trả về 1 EmployeeDTO
     */
    public EmployeeDTO mapToEmpDTO(Employee employee) {
        //Tạo 1 employeeDTO
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
     *
     * @param addEmployeeRequest
     * @param department
     * @return Employee
     */
    public Employee mapToAddemployeeRequestToEmployee(AddEmployeeRequest addEmployeeRequest, Department department) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        Date employeeBirdthDate;
        try {
            employeeBirdthDate = sdf.parse(addEmployeeRequest.getEmployeeBirthDate());
        } catch (ParseException e) {
            throw new EmployeeAddException("ER011-生年月日");
        }
        return Employee.builder()
                .employeeName(addEmployeeRequest.getEmployeeName())
                .employeeId(addEmployeeRequest.getEmployeeId())
                .employeeTelephone(addEmployeeRequest.getEmployeeTelephone())
                .employeeEmail(addEmployeeRequest.getEmployeeEmail())
                .employeeNameKana(addEmployeeRequest.getEmployeeNameKana())
                .employeeLoginPassword(addEmployeeRequest.getEmployeeLoginPassword())
                .employeeLoginId(addEmployeeRequest.getEmployeeLoginId())
                .employeeBirthDate(employeeBirdthDate)
                .department(department)
                .build();
    }

    /**
     * Xử lý việc chuyển 1 EmployeeCertificationReq thành 1 EmployeeCertification
     *
     * @param req           EmployeeCertificationReq cần chuyển đổi
     * @param certification certification chứng chỉ tiếng nhật
     * @param employee      employee
     * @return EmployeeCertification
     */
    public EmployeeCertification mapEmployeeCertificationReqToEmCertificate(EmployeeCertificationReq req, Certification
            certification, Employee employee) {
        //Đối tượng DateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        Date endDate;
        Date startDate;
        try {
            startDate = sdf.parse(req.getCertificationStartDate());
        } catch (ParseException e) {
            throw new EmployeeAddException("ER005-資格交付日-yyyy/MM/dd");
        }
        try {
            endDate = sdf.parse(req.getCertificationEndDate());
            if (endDate.before(startDate) || endDate.equals(startDate)) {
                throw new EmployeeAddException("ER012");
            }
        } catch (ParseException e) {
            throw new EmployeeAddException("ER005-失効日-yyyy/MM/dd");

        }

        return EmployeeCertification.builder()
                .certification(certification)
                .startDate(startDate)
                .endDate(endDate)
                .score(Long.parseLong(req.getEmployeeCertificationScore()))
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
    public List<Sort.Order> getSortOrders(String[] fields, String[] directions) {
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

    /**
     * Phương thức kiểm tra ngày có hợp lệ không
     *
     * @param dateInput chuỗi date đầu vào
     * @return boolean
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

    /**
     * Xử lý việc map từ 1 EmployeeCertification về 1 EmployeeCertificationDTO
     *
     * @param employeeCertification Dữ liệu EmployeeCertification cần chuyển
     * @return EmployeeCertificationDTO
     */
    public EmployeeCertificationDTO mapToEmployeeCertificationDTO(EmployeeCertification employeeCertification) {

        return EmployeeCertificationDTO.builder()
                .certificationId(employeeCertification.getCertification().getCertificationId())
                .certificationStartDate(employeeCertification.getStartDate())
                .certificationEndDate(employeeCertification.getEndDate())
                .employeeCertificationScore(employeeCertification.getScore())
                .certificationName(employeeCertification.getCertification().getCertificationName())
                .build();
    }

    public EmployeeResponse getEmployees(EmployeeRequest employeeRequest) {


        Page<Employee> list;
        Pageable pageable = PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),
                Integer.parseInt(employeeRequest.getLimit()));

        if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
        ) {
            Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                    .orElseThrow();
            list = employeeRepo.findByEmployeeNameAndDepartmentIdSort(employeeRequest.getEmployee_name()
                    ,Long.valueOf(employeeRequest.getDepartment_id()),pageable,
                    employeeRequest.getOrd_employee_name(),employeeRequest.getOrd_certification_name(),
                    employeeRequest.getOrd_end_date());
        } else if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                || (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))) {
            if (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals("")) {
                Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                        .orElseThrow();
                list = employeeRepo.findByDepartmentIdSort(Long.valueOf(employeeRequest.getDepartment_id()),pageable,
                        employeeRequest.getOrd_employee_name(),employeeRequest.getOrd_certification_name(),
                        employeeRequest.getOrd_end_date());
            } else {
                list = employeeRepo.findByEmployeeNameSort(employeeRequest.getEmployee_name(),pageable,
                        employeeRequest.getOrd_employee_name(),employeeRequest.getOrd_certification_name(),
                        employeeRequest.getOrd_end_date());
            }

        } else {
            list = employeeRepo.findAllAndSort(
                    employeeRequest.getOrd_employee_name(),
                    employeeRequest.getOrd_certification_name(),
                    employeeRequest.getOrd_end_date(),pageable);
        }


        List<EmployeeDTO> employeeDTOList = list.getContent().stream().map(this::mapToEmpDTO).collect(Collectors.toList());

        return EmployeeResponse.builder()
                .code(200)
                .totalRecords((int) list.getTotalElements())
                .employees(employeeDTOList)
                .build();
    }




}
