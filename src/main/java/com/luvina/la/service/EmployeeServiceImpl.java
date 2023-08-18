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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.stream.Collectors;

import static com.luvina.la.config.Constants.*;


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
    private final ValidateService validateService;

    /**
     * Xử lý việc get danh sách employee theo các điều kiện của EmployeeRequest
     *
     * @param employeeRequest chứa các thuộc tính là các điều kiện để thực hiện request
     * @return Trả về 1 EmployeeResponse
     */
    @Override
    public EmployeeResponse getEmployee(EmployeeRequest employeeRequest) {


        Page<Employee> list;
        Pageable pageable = PageRequest.of(Integer.parseInt(employeeRequest.getOffset()),
                Integer.parseInt(employeeRequest.getLimit()));
        //Kiểm tra các trường employee_name,departmentId xem có null hoặc rỗng không
        if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                && (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))
        ) {
            Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                    .orElseThrow();
            list = employeeRepo.findByEmployeeNameAndDepartmentIdSort(employeeRequest.getEmployee_name()
                    , Long.valueOf(employeeRequest.getDepartment_id()), pageable,
                    employeeRequest.getOrd_employee_name(), employeeRequest.getOrd_certification_name(),
                    employeeRequest.getOrd_end_date());
        } else if ((employeeRequest.getEmployee_name() != null && !employeeRequest.getEmployee_name().equals(""))
                || (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals(""))) {
            if (employeeRequest.getDepartment_id() != null && !employeeRequest.getDepartment_id().equals("")) {
                Department department = departmentRepo.findById(Long.valueOf(employeeRequest.getDepartment_id()))
                        .orElseThrow();
                list = employeeRepo.findByDepartmentIdSort(Long.valueOf(employeeRequest.getDepartment_id()), pageable,
                        employeeRequest.getOrd_employee_name(), employeeRequest.getOrd_certification_name(),
                        employeeRequest.getOrd_end_date());
            } else {
                list = employeeRepo.findByEmployeeNameSort(employeeRequest.getEmployee_name(), pageable,
                        employeeRequest.getOrd_employee_name(), employeeRequest.getOrd_certification_name(),
                        employeeRequest.getOrd_end_date());
            }

        } else {
            list = employeeRepo.findAllAndSort(
                    employeeRequest.getOrd_employee_name(),
                    employeeRequest.getOrd_certification_name(),
                    employeeRequest.getOrd_end_date(), pageable);
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
        validateService.validateEmployeeLoginId(addEmployeeRequest.getEmployeeLoginId());
        //Throw exception nếu employeeName không hợp lệ
        validateService.validateEmployeeName(addEmployeeRequest.getEmployeeName());
        //Throw exception nếu employeeNameKana không hợp lệ
        validateService.validateEmployeeNameKana(addEmployeeRequest.getEmployeeNameKana());
        //Throw exception nếu employeeBirthDate không hợp lệ
        validateService.validateEmployeeBirthDate(addEmployeeRequest.getEmployeeBirthDate());
        //Throw exception nếu employeeEmail không hợp lệ
        validateService.validateEmployeeEmail(addEmployeeRequest.getEmployeeEmail());
        //Throw exception nếu employeeTelephone không hợp lệ
        validateService.validateEmployeePhone(addEmployeeRequest.getEmployeeTelephone());
        //Throw exception nếu employeeLoginPassword không hợp lệ
        validateService.validateEmployeeLoginPassword(addEmployeeRequest.getEmployeeLoginPassword());
        //Throw exception nếu departmentId không hợp lệ
        validateService.validateDepartmentId(addEmployeeRequest.getDepartmentId());
        //Throw exception nếu Certifications không hợp lệ
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        if (addEmployeeRequest.getCertifications().size() > 0) {
            addEmployeeRequest.getCertifications().forEach(validateService::validateCertification);
        }
        //mã hoá password
        addEmployeeRequest.setEmployeeLoginPassword(passwordEncoder.encode(addEmployeeRequest.getEmployeeLoginPassword()
        ));
        Department department = departmentRepo.findById(Long.parseLong(addEmployeeRequest.getDepartmentId()))
                .orElseThrow(() -> new EmployeeAddException(ER004 + "-" + LABEL_EMP_DEPARTMENT));
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
                new EmployeeAddException(ER013 + "-" + LABEL_ID));
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
                    .sorted(Comparator.comparing(cer -> cer.getCertification().getCertificationLevel())).toList()
                    .stream().map(this::mapToEmployeeCertificationDTO).collect(Collectors.toList()));
        } else {
            employeeGetByIDResponse.setCertifications(List.of());
        }
        return employeeGetByIDResponse;
    }

    /**
     * Xử lý việc xoá 1 employee theo id của employee
     *
     * @param employeeId id của employee cần xoá
     * @return employeeId id của employee
     */
    @Override
    @Transactional
    public Long deleteEmployee(Long employeeId) {
        //Kiểm tra xem có tồn tại employee có id này trong db không
        Employee emp = employeeRepo.findByEmployeeId(employeeId).orElseThrow(() -> new EmployeeAddException(ER014 + "-"
                + LABEL_ID));
        employeeRepo.delete(emp);
        return employeeId;
    }

    /**
     * Xử lý việc update 1 employee
     *
     * @param addEmployeeRequest
     * @return employee thông tin của employee vừa update được
     */
    @Override
    @Transactional
    public Employee editEmployee(AddEmployeeRequest addEmployeeRequest) {
        //Throw exception nếu EmployeeId không hợp lệ
        validateService.validateEmployeeId(addEmployeeRequest.getEmployeeId());
        Employee employee = employeeRepo.findByEmployeeId(addEmployeeRequest.getEmployeeId())
                .orElseThrow(() -> new EmployeeAddException(ER013+"-"+LABEL_ID));
        //Throw exception nếu EmployeeLoginId không hợp lệ
        if (addEmployeeRequest.getEmployeeLoginId() == null) {
            throw new EmployeeAddException(ER001+"-"+LABEL_EMP_LOGINID);
        }
        if (!addEmployeeRequest.getEmployeeLoginId().equals(employee.getEmployeeLoginId())) {
           validateService.validateEmployeeLoginId(addEmployeeRequest.getEmployeeLoginId());
        }
        //Throw exception nếu employeeName không hợp lệ
        validateService.validateEmployeeName(addEmployeeRequest.getEmployeeName());
        //Throw exception nếu employeeNameKana không hợp lệ
        validateService.validateEmployeeNameKana(addEmployeeRequest.getEmployeeNameKana());
        //Throw exception nếu employeeBirthDate không hợp lệ
        validateService.validateEmployeeBirthDate(addEmployeeRequest.getEmployeeBirthDate());
        //Throw exception nếu employeeEmail không hợp lệ
        validateService.validateEmployeeEmail(addEmployeeRequest.getEmployeeEmail());
        //Throw exception nếu employeeTelephone không hợp lệ
        validateService.validateEmployeePhone(addEmployeeRequest.getEmployeeTelephone());
        //Throw exception nếu employeeLoginPassword không hợp lệ

        if (addEmployeeRequest.getEmployeeLoginPassword() == null) {
            throw new EmployeeAddException(ER001+"-"+LABEL_EMP_PASSWORD);
        }
        if (addEmployeeRequest.getEmployeeLoginPassword().equals("")) {
            addEmployeeRequest.setEmployeeLoginPassword(employee.getEmployeeLoginPassword());

        }
        if (!addEmployeeRequest.getEmployeeLoginPassword().equals(employee.getEmployeeLoginPassword())) {
            if ((addEmployeeRequest.getEmployeeLoginPassword().length() > 50
                    || addEmployeeRequest.getEmployeeLoginPassword().length() < 8)) {
                throw new EmployeeAddException(ER007+"-"+LABEL_EMP_PASSWORD+"-8-50");
            }
        }
        //Throw exception nếu departmentId không hợp lệ

        if (addEmployeeRequest.getDepartmentId() == null) {
            throw new EmployeeAddException("ER002-グループ");
        }
        Long departId;
        try {
            departId = Long.parseLong(addEmployeeRequest.getDepartmentId());
        } catch (NumberFormatException ex) {
            throw new EmployeeAddException("ER018-グループ");
        }
        if (departId <= 0) {
            throw new EmployeeAddException("ER018-グループ");
        }
        //Kiểm tra xem có department trong db không
        Department department = departmentRepo.findById(Long.parseLong(addEmployeeRequest.getDepartmentId()))
                .orElseThrow(() -> new EmployeeAddException("ER004-グループ"));

        //Throw exception nếu Certifications không hợp lệ
        if (addEmployeeRequest.getCertifications().size() > 0) {
            addEmployeeRequest.getCertifications().forEach(validateService::validateCertification);
        }

        //Mã hoá password nếu password khác
        if (!addEmployeeRequest.getEmployeeLoginPassword().equals(employee.getEmployeeLoginPassword())) {
            employee.setEmployeeLoginPassword(passwordEncoder.encode(addEmployeeRequest
                    .getEmployeeLoginPassword()
            ));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        Date employeeBirdthDate;
        if (!checkDateValid(addEmployeeRequest.getEmployeeBirthDate())) {
            throw new EmployeeAddException(ER011 + "-" + LABEL_EMP_BIRTHDATE);
        }
        try {
            employeeBirdthDate = sdf.parse(addEmployeeRequest.getEmployeeBirthDate());
        } catch (ParseException e) {
            throw new EmployeeAddException(ER005 + "-" + LABEL_EMP_BIRTHDATE + "-" + "yyyy/MM/dd");
        }
        //Xét lại giá trị cho employee
        employee.setEmployeeName(addEmployeeRequest.getEmployeeName());
        employee.setEmployeeEmail(addEmployeeRequest.getEmployeeEmail());
        employee.setEmployeeLoginId(addEmployeeRequest.getEmployeeLoginId());
        employee.setDepartment(department);
        employee.setEmployeeBirthDate(employeeBirdthDate);
        employee.setEmployeeNameKana(addEmployeeRequest.getEmployeeNameKana());
        employee.setEmployeeTelephone(addEmployeeRequest.getEmployeeTelephone());

        List<EmployeeCertification> employeeCertificationList = new ArrayList<>();
        //kiểm tra xem employee có certification không
        if (addEmployeeRequest.getCertifications().size() > 0) {
            for (EmployeeCertificationReq e : addEmployeeRequest.getCertifications()) {
                EmployeeCertification employeeCertification = new EmployeeCertification();
                Certification certification = new Certification();
                certification = certificationRepo.findById(Long.parseLong(e.getCertificationId())).orElseThrow();
                employeeCertification = mapEmployeeCertificationReqToEmCertificate(e, certification, employee);
                employeeCertificationList.add(employeeCertification);
            }
        }
        Employee editEmployee = employeeRepo.save(employee);
        //Xóa các hết các dữ liệu ở bảng employees_certifications của employee có employeeId cần update
        employeeCertificationRepo.deleteByEmployeeEmployeeId(editEmployee.getEmployeeId());

        //Lưu các certification
        if (employeeCertificationList.size() > 0) {
            employeeCertificationList.forEach(employeeCertificationRepo::save);
        }
        return editEmployee;
    }


    /**
     * Xử lý việc map từ 1 Employee sang EmployeeDTO
     *
     * @param employee Thông tin của 1 Employee
     * @return Trả về 1 EmployeeDTO thông tin về EmployeeDTO sau khi map
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

        //Lấy ra danh sách EmployeeCertification
        List<EmployeeCertification> employeeCertifications = employee.getEmployeeCertification();
        //Kiểm tra xem certification không
        if (!employeeCertifications.isEmpty()) {
            EmployeeCertification firstCertification = employeeCertifications.stream()
                    .sorted(Comparator.comparing(cer -> cer.getCertification().getCertificationLevel())).toList().get(0);
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
     * @return Employee thông tin của employee sau khi map
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
     * @return EmployeeCertification thông tin của EmployeeCertification sau khi map
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

    /**
     * Xử lý việc map từ 1 EmployeeCertification về 1 EmployeeCertificationDTO
     *
     * @param employeeCertification Dữ liệu EmployeeCertification cần chuyển
     * @return EmployeeCertificationDTO thông tin của chứng chỉ
     */
    public EmployeeCertificationDTO mapToEmployeeCertificationDTO(EmployeeCertification employeeCertification) {
        return EmployeeCertificationDTO.builder()
                .certificationId(employeeCertification.getCertification().getCertificationId())
                .certificationStartDate(employeeCertification.getStartDate())
                .certificationEndDate(employeeCertification.getEndDate())
                .employeeCertificationScore(employeeCertification.getScore())
                .build();
    }
}
