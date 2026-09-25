package com.rps.finwallet.repository;

import com.rps.finwallet.model.SalaryDisbursement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryDisbursementRepository extends JpaRepository<SalaryDisbursement, Long> {

    /**
     * Idempotency Check: Ensures an employee is never paid more than once for a given month!
     */
    boolean existsByEmployeeIdAndSalaryMonth(Long employeeId, String salaryMonth);

    Optional<SalaryDisbursement> findByReferenceNumber(String referenceNumber);

    List<SalaryDisbursement> findBySalaryMonth(String salaryMonth);

    Page<SalaryDisbursement> findByEmployeeIdOrderByDisbursedAtDesc(Long employeeId, Pageable pageable);
}
