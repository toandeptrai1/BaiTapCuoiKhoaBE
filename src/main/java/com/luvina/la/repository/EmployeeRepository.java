/**
 * Copyright(C) 2023 Luvina Software Company
 * EmployeeRepository.java, June 29, 2023 Toannq
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface EmployeeRepository kế thừa lại các phương thức của JpaRepository
 * giúp thực hiện các câu truy vấn sql liên quan đến bảng employees
 *
 * @author Toannq
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {


    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);

    Optional<Employee> findByEmployeeId(Long employeeId);

    /**
     * Xử lý việc tìm 1 employee thỏa mãn departmentID and EmployeeName %like%
     *
     * @param department   department cần tìm kiếm
     * @param employeeName employee cần tìm kiếm
     * @param pageable     đối tượng pageable khai báo page,size,sort dùng để phân trang
     * @return 1 Page<Employee>
     */
    Page<Employee> findByDepartmentOrEmployeeNameContaining(Department department, String employeeName, Pageable pageable);

    /**
     * Xử lý việc tìm 1 employee thỏa mãn departmentID or EmployeeName %like%
     *
     * @param department   department cần tìm kiếm
     * @param employeeName employee cần tìm kiếm
     * @param pageable     đối tượng pageable khai báo page,size,sort dùng để phân trang
     * @return 1 Page<Employee>
     */
    Page<Employee> findByDepartmentAndEmployeeNameContaining(Department department, String employeeName, Pageable pageable);


    /** Thực hiện: Sort theo sortEmployeeName, sortCertificationName, sortEndDate khi tìm kiếm theo cả employeeName và departmentId
     * @param employeeName: tìm theo tên nhân viên
     *  @param departmentId: tìm theo tên nhân viên
     * @param pageable: để phân trang
     *  @param sortEmployeeName: để sort theo employeeName
     *  @param sortCertificationName: để sort theo CertificationName
     *  @param sortEndDate: để sort theo EndDate
     */
    @Query(value = "SELECT e FROM Employee e " +
            "LEFT JOIN e.department d " +
            "LEFT JOIN e.employeeCertification ec " +
            "LEFT JOIN ec.certification c " +
            "WHERE e.employeeName LIKE %:employeeName% AND " +
            "d.departmentId = :departmentId " +
            "AND (c.certificationLevel IS NULL OR c.certificationLevel = (" +
            "    SELECT MIN(c2.certificationLevel) " +
            "    FROM Employee e2 " +
            "    LEFT JOIN e2.employeeCertification ec2 " +
            "    LEFT JOIN ec2.certification c2 " +
            "    WHERE e2.employeeId = e.employeeId" +
            ")) " +
            "ORDER BY " +
            "CASE WHEN :sortEmployeeName = 'ASC' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortEmployeeName = 'DESC' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortCertificationName = 'ASC' THEN c.certificationName END ASC, " +
            "CASE WHEN :sortCertificationName = 'DESC' THEN c.certificationName END DESC, " +
            "CASE WHEN :sortEndDate = 'ASC' THEN ec.endDate END ASC, " +
            "CASE WHEN :sortEndDate = 'DESC' THEN ec.endDate END DESC")
    Page<Employee> findByEmployeeNameAndDepartmentIdSort(String employeeName, Long departmentId, Pageable pageable,
                                                         @Param("sortEmployeeName") String sortEmployeeName,
                                                         @Param("sortCertificationName") String sortCertificationName,
                                                         @Param("sortEndDate") String sortEndDate);
    /**
     * Thực hiện: Sort theo sortEmployeeName, sortCertificationName, sortEndDate khi tìm kiếm theo employeeName
     * @param employeeName: tìm theo tên nhân viên
     * @param pageable: để phân trang
     *  @param sortEmployeeName: để sort theo employeeName
     *  @param sortCertificationName: để sort theo CertificationName
     *  @param sortEndDate: để sort theo EndDate
     */
    @Query(value = "SELECT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeCertification ec " +
            "LEFT JOIN ec.certification c " +
            "WHERE e.employeeName LIKE %:employeeName% " +
            "AND (c.certificationLevel IS NULL OR c.certificationLevel = (" +
            "    SELECT MIN(c2.certificationLevel) " +
            "    FROM Employee e2 " +
            "    LEFT JOIN e2.employeeCertification ec2 " +
            "    LEFT JOIN ec2.certification c2 " +
            "    WHERE e2.employeeId = e.employeeId" +
            ")) " +
            "ORDER BY " +
            "CASE WHEN :sortEmployeeName = 'ASC' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortEmployeeName = 'DESC' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortCertificationName = 'ASC' THEN c.certificationName END ASC, " +
            "CASE WHEN :sortCertificationName = 'DESC' THEN c.certificationName END DESC, " +
            "CASE WHEN :sortEndDate = 'ASC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END ASC, " +
            "CASE WHEN :sortEndDate = 'DESC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END DESC")
    Page<Employee> findByEmployeeNameSort(String employeeName, Pageable pageable,
                                          @Param("sortEmployeeName") String sortEmployeeName,
                                          @Param("sortCertificationName") String sortCertificationName,
                                          @Param("sortEndDate") String sortEndDate);

    /**
     * Thực hiện: Sort theo sortEmployeeName, sortCertificationName, sortEndDate khi tìm kiếm theo employeeName
     * @param departmentId: tìm theo departmentId
     * @param pageable: để phân trang
     *  @param sortEmployeeName: để sort theo employeeName
     *  @param sortCertificationName: để sort theo CertificationName
     *  @param sortEndDate: để sort theo EndDate
     */
    @Query(value = "SELECT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeCertification ec " +
            "LEFT JOIN ec.certification c " +
            "WHERE (e.department.departmentId = :departmentId OR e.department IS NULL) " +
            "AND (c.certificationLevel IS NULL OR c.certificationLevel = (" +
            "    SELECT MIN(c2.certificationLevel) " +
            "    FROM Employee e2 " +
            "    LEFT JOIN e2.employeeCertification ec2 " +
            "    LEFT JOIN ec2.certification c2 " +
            "    WHERE e2.employeeId = e.employeeId" +
            ")) " +
            "ORDER BY " +
            "CASE WHEN :sortEmployeeName = 'ASC' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortEmployeeName = 'DESC' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortCertificationName = 'ASC' THEN c.certificationName END ASC, " +
            "CASE WHEN :sortCertificationName = 'DESC' THEN c.certificationName END DESC, " +
            "CASE WHEN :sortEndDate = 'ASC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END ASC, " +
            "CASE WHEN :sortEndDate = 'DESC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END DESC")
    Page<Employee> findByDepartmentIdSort(Long departmentId, Pageable pageable,
                                          @Param("sortEmployeeName") String sortEmployeeName,
                                          @Param("sortCertificationName") String sortCertificationName,
                                          @Param("sortEndDate") String sortEndDate);

    /**
     * Thực hiện: Sort theo sortEmployeeName, sortCertificationName, sortEndDate
     * @param pageable: để phân trang
     *  @param sortEmployeeName: để sort theo employeeName
     *  @param sortCertificationName: để sort theo CertificationName
     *  @param sortEndDate: để sort theo EndDate
     */
    @Query(value = "SELECT e FROM Employee e " +
            "LEFT JOIN e.department d " +
            "LEFT JOIN e.employeeCertification ec " +
            "LEFT JOIN ec.certification c " +
            "WHERE c.certificationLevel IS NULL OR c.certificationLevel = (" +
            "    SELECT MIN(c2.certificationLevel) " +
            "    FROM Employee e2 " +
            "    LEFT JOIN e2.employeeCertification ec2 " +
            "    LEFT JOIN ec2.certification c2 "+
            "    WHERE e2.employeeId = e.employeeId" +
            ") "+
            "ORDER BY " +
            "CASE WHEN :sortEmployeeName = 'ASC' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortEmployeeName = 'DESC' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortCertificationName = 'ASC' THEN c.certificationName END ASC, " +
            "CASE WHEN :sortCertificationName = 'DESC' THEN c.certificationName END DESC, " +
            "CASE WHEN :sortEndDate = 'ASC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END ASC, " +
            "CASE WHEN :sortEndDate = 'DESC' THEN COALESCE(ec.endDate, STR_TO_DATE('9999-12-31', '%Y-%m-%d')) END DESC")
    Page<Employee> findAllAndSort(@Param("sortEmployeeName") String sortEmployeeName,
                                  @Param("sortCertificationName") String sortCertificationName,
                                  @Param("sortEndDate") String sortEndDate,
                                  Pageable pageable);


}
